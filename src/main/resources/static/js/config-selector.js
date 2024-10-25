// This function updates the Config Value input based on the selected Config Type
function updateConfigValueField() {
    const configTypeSelect = document.getElementById('configType');
    const configValueContainer = document.getElementById('configValueContainer');
    const selectedType = configTypeSelect.options[configTypeSelect.selectedIndex].text;

    let inputField;

    switch (selectedType) {
        case 'Boolean':
            inputField = `<select class="form-control" id="configValue" name="configValue">
                            <option value="true">True</option>
                            <option value="false">False</option>
                          </select>`;
            break;
        case 'Number':
            inputField = `<input type="number" class="form-control" id="configValue" name="configValue" required>`;
            break;
        case 'Date':
            inputField = `<input type="date" class="form-control" id="configValue" name="configValue" required>`;
            break;
        case 'Color':
            inputField = `<input type="color" class="form-control" id="configValue" name="configValue" required>`;
            break;
        case 'Double':
            inputField = `<input type="number" step="0.01" class="form-control" id="configValue" name="configValue" required>`;
            break;
        case 'List':
            inputField = `<textarea class="form-control" id="configValue" name="configValue" rows="3" placeholder="Enter comma-separated values" required></textarea>`;
            break;
        default:
            inputField = `<input type="text" class="form-control" id="configValue" name="configValue" placeholder="Enter value" required>`;
    }

    configValueContainer.innerHTML = `
        <label for="configValue" class="form-group">Config Value</label>
        ${inputField}
    `;
}