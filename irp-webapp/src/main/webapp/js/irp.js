(function() {
    this.irp = {
        created: function() {
            this.response = {};
        },
        status: function (input) {
            if (!input) {
                return "";
            }
            if (input.enumfieldCheckType == 'D') {
                return "Ignored";
            }

            var valid = !input.fieldEmpty && input.correctDataType && input.acceptableValue;

            if (input.enumfieldCheckType == 'PC') {
                valid = valid && input.correctValue;
            }

            return valid ? "No Errors" : "Errors Found";
        }
        ,
        explanation: function (input) {
            if (!input) {
                return "";
            }
            if (input.enumfieldCheckType == 'D') {
                return "";
            }

            var reasons = [input.enumfieldCheckType];
            if (input.fieldEmpty) reasons.push("Field Empty");
            if (!input.correctDataType) reasons.push("Incorrect Data Type");
            if (!input.acceptableValue) reasons.push("Unacceptable Value");

            if (input.enumfieldCheckType == 'PC') {
                if (!input.correctValue) reasons.push("Incorrect Value");
            }

            return reasons.join();
        }
        ,
        oddOrEvenClass: function (index) {
            return index % 2 == 0 ? 'even' : 'odd';
        }
        ,
        itemStatus: function (item) {
            return (item.status == 'FOUND' && item.itemFormatCorrect) ? "Match" : "Errors Found";

        }
        ,
        itemFound: function (item) {
            return item.status == 'FOUND';
        }
        ,
        itemExplanation: function (item) {
            if (item.status == 'FOUND' && item.itemFormatCorrect) {
                return 'This Item matches a given IRP Item. The detailed analysis follows.';
            }
            if (item.status == 'FOUND' && !item.itemFormatCorrect) {
                return "The Item's format is incorrect";
            }
            if (item.status == 'MISSING') {
                return 'This IRP Item is missing from the TDS Report.';
            }
            if (item.status == 'EXTRA') {
                return 'This Item is unknown to IRP or a duplicate of an existing.';
            }
            return '';
        }
    };
}());