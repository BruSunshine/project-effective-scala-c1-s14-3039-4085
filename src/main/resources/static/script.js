
let operationCount = 0;
let operations = {};

function addOperation() {
    const operationType = document.getElementById('operationType').value;
    const operand1 = document.getElementById('operand1').value;
    const operand2 = operationType !== 'Num' ? document.getElementById('operand2').value : null;

    const newOperationId = `op-${operationCount}`;
    const newOperation = { type: operationType, operand1, operand2, nestedIn: null };
    operations[newOperationId] = newOperation;

    const nestUnder = document.getElementById('nestUnder').value;
    const nestOperand = document.getElementById('nestOperand').value;
    if (nestUnder !== "none") {
        operations[newOperationId].nestedIn = { operation: nestUnder, operand: nestOperand };
        operations[nestUnder][`operand${nestOperand}`] = newOperationId;
    }

    operationCount++;
    updateOperationsDisplay();
    resetFormInputs();
    updateNestUnderOptions();
}

function updateOperationsDisplay() {
    const display = document.getElementById('operationsDisplay');
    display.textContent = JSON.stringify(operations, null, 2);
}

function resetFormInputs() {
    document.getElementById('operand1').value = '';
    document.getElementById('operand2').value = '';
    document.getElementById('nestUnder').value = 'none';
    document.getElementById('nestOperand').value = '1';
    handleOperationTypeChange();
}

function updateNestUnderOptions() {
    const nestUnder = document.getElementById('nestUnder');
    nestUnder.innerHTML = '<option value="none">None (root)</option>';
    for (const operationId in operations) {
        nestUnder.innerHTML += `<option value="${operationId}">${operationId} (${operations[operationId].type})</option>`;
    }
}

function translateJSONStructure() {
    function translateNode(nodeId) {
        const node = operations[nodeId];
        let newNode = {};

        if (node.type === 'Num') {
            // Parse operand1 to a number if it's a valid number, otherwise leave it as a string
            const parsedOperand = isNaN(node.operand1) ? node.operand1 : parseFloat(node.operand1);
            //newNode = {
            //    "$type": "startup.ast.Num",
            //    "n": parsedOperand//{ "name": node.operand1 }
            //};
                    // Check if parsedOperand is a string and adjust newNode structure accordingly
            if (typeof parsedOperand === 'string') {
                newNode = {
                    "$type": "startup.ast.Num",
                    "n": { "name": parsedOperand }
                };
            } else {
                newNode = {
                    "$type": "startup.ast.Num",
                    "n": parsedOperand
                };
            }
        } else {
            // If operand is a reference to another operation, recursively translate it
            const a = isOperationId(node.operand1) ? translateNode(node.operand1) : node.operand1;
            const b = isOperationId(node.operand2) ? translateNode(node.operand2) : node.operand2;

            newNode = {
                "$type": `startup.ast.${node.type}`,
                "a": a
            };

            if (node.type !== 'Num') {
                newNode.b = b;
            }
        }

        return newNode;
    }

    function isOperationId(operand) {
        // Check if operand is a reference to another operation
        return typeof operand === 'string' && operand.startsWith('op-');
    }

    const rootNodeId = Object.keys(operations).find(id => !operations[id].nestedIn);
    const newJsonStructure = translateNode(rootNodeId);
    
    const translatedDisplay = document.getElementById('translatedDisplay');
    translatedDisplay.textContent = JSON.stringify(newJsonStructure, null, 2);

    return newJsonStructure
}

function handleOperationTypeChange() {
    const operationType = document.getElementById('operationType').value;
    const operand2Input = document.getElementById('operand2');
    if (operationType === 'Num') {
        operand2Input.style.display = 'none';
    } else {
        operand2Input.style.display = '';
    }
}
function resetForm() {
    document.getElementById('operationType').value = 'Plus'; // Default value
    document.getElementById('operand1').value = '';
    document.getElementById('operand2').value = '';
    document.getElementById('nestUnder').value = 'none';
    document.getElementById('nestOperand').value = '1';
    handleOperationTypeChange(); // This will handle the visibility of the operand2 input based on the operation type
    // Clear the content of the Original Structure and Translated Structure displays
    document.getElementById('operationsDisplay').textContent = '';
    document.getElementById('translatedDisplay').textContent = '';

    // Additionally, clear the operations object if you want to reset the entire form state
    operations = {};
    operationCount = 0;
    updateNestUnderOptions(); // Reset the options for nesting under other operations

    updateOperationsDisplay(); // Assuming you want to clear the display as well
}

function evaluateExpression() {
    
    const translatedJson = translateJSONStructure();

    let numOperandType = null; // This will store the type of operands in Num nodes

    function checkNumOperands(node) {
        if (node.$type === 'startup.ast.Num') {
            let currentType;
            if (typeof node.n === 'object' && node.n.hasOwnProperty('name')) {
                // If n is an object with a name property, treat it as a string
                currentType = 'string';
            } else {
                // Otherwise, use the normal typeof check
                currentType = typeof node.n;
            }

            if (numOperandType === null) {
                numOperandType = currentType;
            } else if (numOperandType !== currentType) {
                return false;
            }
        }

        if (node.a && typeof node.a === 'object' && !checkNumOperands(node.a)) return false;
        if (node.b && typeof node.b === 'object' && !checkNumOperands(node.b)) return false;
        return true;
    }
    if (!checkNumOperands(translatedJson)) {
        alert("Error: Mixed operand types in Num operations are not allowed.");
        console.error("Error: Mixed operand types in Num operations are not allowed.");
        return; // Stop the function execution if mixed types are found
    }

    const endpointEval = numOperandType === 'string' ? '/evaluate2' : '/evaluate1';
    const endpointResult = numOperandType === 'string' ? '/resultsstring2' : '/resultsstring1';

    const translatedJsonStructured = {"argast": [1, translatedJson]};
    const translatedJsonstringified = JSON.stringify(translatedJsonStructured);

    fetch(endpointEval, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: translatedJsonstringified
    })
    .then(response => response.json())
    .then(data => {
        if (data.error) {
            throw new Error(data.error);
        }
        window.location.href = endpointResult;//'/resultsstring';
    })
    .catch(error => {
        console.error('Error:', error);
        alert(error.message);
    });
}

document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('addOperationButton').addEventListener('click', addOperation);
    document.getElementById('translateButton').addEventListener('click', translateJSONStructure);
    document.getElementById('operationType').addEventListener('change', handleOperationTypeChange);
    updateNestUnderOptions();
    document.getElementById('evaluateButton').addEventListener('click', evaluateExpression);
    document.getElementById('resetButton').addEventListener('click', resetForm);
});