(function(document) {
    'use strict';

    // Grab a reference to our auto-binding template
    // and give it some initial binding values
    // Learn more about auto-binding templates at http://goo.gl/Dx1u2g
    var app = document.querySelector('#app');

    // Sets app default base URL
    app.baseUrl = '/';
    if (window.location.port === '') {  // if production
        // Uncomment app.baseURL below and
        // set app.baseURL to '/your-pathname/' if running from folder in production
        // app.baseUrl = '/polymer-starter-kit/';
    }

    // Listen for template bound event to know when bindings
    // have resolved and content has been stamped to the page
    app.addEventListener('dom-change', function() {
        console.info('Implementation Readiness Package is running.');
    });

    // See https://github.com/Polymer/polymer/issues/1381
    window.addEventListener('WebComponentsReady', function() {
        // imports are loaded and elements have been registered

        var that = app;

        // Manual Mode events
        app.selected = 0;
        app.grade = 3;
        app.subject = 'ela';

        // Determines current mode
        // Used to show summary table for the correct mode (either manual or automation)
        app.isAutomation = true;
        
        app.$.btnFileUpload.addEventListener('click', function (event) {
            if (that.$.clientName.value != null && that.$.clientName.value != '') {
                this.hidden = true;
                that.$.spinnerUpload.active = true;
            }
        });
        app.$.catBtnFileUpload.addEventListener('click', function (event) {
            if (that.$.catItemFile.value && that.$.catStudentFile.value) {
                console.log(that.$.catItemFile.value);
                console.log(that.$.catStudentFile.value);
                this.hidden = true;
                that.$.catSpinnerUpload.active = true;
            }
        });
        app.$.fileUploader.addEventListener('submitted', function (event) {
            that.$.btnFileUpload.hidden = false;
            that.$.spinnerUpload.active = false;
            if (event.detail.status == 200) {
                var analysisResponse = JSON.parse(event.detail.responseText);
                that.responses = analysisResponse;
                app.isAutomation = false;
            } else {
                that.$.fileUploadErrorDialog.toggle();
            }
        });

        app.$.catFileUploader.addEventListener('submitted', function (event) {
            that.$.catBtnFileUpload.hidden = false;
            that.$.catSpinnerUpload.active = false;
            if (event.detail.status == 200) {
                // Parse response data
                that.catResults = JSON.parse(event.detail.responseText);
            } else {
                // Show some error message
                that.$.fileUploadErrorDialog.toggle();
            }
        });
        
        app.$.catGradeSelector.addEventListener('change', function(event) {
            that.grade = this.options[this.selectedIndex].value;
            that.catResults = null;
        });
        
        app.$.catSubjectSelector.addEventListener('change', function(event) {
            that.subject = this.options[this.selectedIndex].value;
            that.catResults = null;
        });

        // Automation Mode events
        window.addEventListener('message', function(event) {
            // Message handler for receiving messages from the Adapter iframe
            var adapterIFrame = that.$.adapterIFrame;
            var origin = event.origin || event.originalEvent.origin;
            if (!(adapterIFrame && adapterIFrame.src && adapterIFrame.src.startsWith(origin))) {
                console.error("Message received was not from same window");
                return;
            }

            if (!Array.isArray(event.data)) {
                console.error("Data is not an array");
                that.$.adapterInterfaceMessages.innerHTML = '<p>Error: The data received from that Adapter UI is not an array</p>';
                adapterIFrame.hidden = true;
                that.$.dlgAdapterInterface.notifyResize();
                that.$.dlgAdapterInterface.center();
                return;
            }

            if (performAnalysis(event.data)) {
                that.$.dlgAdapterInterface.close();
                that.$.btnBeginAutomation.disabled = false;
            }
        });
        function performAnalysis(tdsReportLinks) {
            console.info("Sending data to IRP Server for Analysis");
            var vendorName = that.$.adapterVendorName.value;
            that.$.ajaxAutomation.body = { vendorName: vendorName, tdsReportLinks: tdsReportLinks };
            that.$.ajaxAutomation.contentType = "application/json";
            that.$.ajaxAutomation.generateRequest();
        }
        app.$.ajaxAutomation.addEventListener('error', function (event) {
            console.error("Error sending Automation Adapter's TDS Report URIs", event.detail.text);
            that.$.adapterInterfaceMessages.innerHTML = '<p>Error: Unable to communicate with IRP</p>';
            adapterIFrame.hidden = true;
            that.$.dlgAdapterInterface.notifyResize();
            that.$.dlgAdapterInterface.center();

        });
        app.$.ajaxAutomation.addEventListener('response', function (event) {
            that.$.dlgAdapterInterface.close();
            that.$.btnBeginAutomation.disabled = false;

            that.responses = event.detail.response;
            app.isAutomation = true;
        });
        app.$.btnBeginAutomation.addEventListener('click', function (event) {
            if (!that.$.adapterVendorName.validate() || !that.$.adapterUrl.validate()) {
                return;
            }

            that.$.btnBeginAutomation.disabled = true;

            adapterIFrame.hidden = false;
            that.$.adapterInterfaceMessages.innerHTML = '';
            that.$.dlgAdapterInterface.open();
            that.$.dlgAdapterInterface.notifyResize();
            that.$.dlgAdapterInterface.center();

            that.$.adapterIFrame.src = that.$.adapterUrl.value;
        });
        app.$.btnAutomationProgressClose.addEventListener('click', function (event) {
            that.$.dlgAdapterInterface.close();
            that.$.btnBeginAutomation.disabled = false;
        });
    });

    app.hasError = function(catData) {
        return catData.error === true;
    }

    // Scroll page to top and expand header
    app.scrollPageToTop = function() {
        app.$.headerPanelMain.scrollToTop(true);
    };

    app.closeDrawer = function() {
        app.$.paperDrawerPanel.closeDrawer();
    };

    app.stringify =  function () {
        return JSON.stringify(value);
    };

    app.computeRoute = function (routeIndex) {
        return 'route-' + routeIndex;
    }

    app.computeResponseUrl = function (responseFileName) {
        return app.baseUrl + 'report/' + responseFileName;
    };

    app.responsesExist = function (responses) {
        return typeof responses !== 'undefined' && responses != null && Object.keys(responses).length > 0;
    };

    app.keyLength = function (obj) {
        return Object.keys(obj).length;
    }

    app.computeResponseIcon = function (response) {
        return response.validXMLfile && response.validTestName && response.validExaminee
            && response.validScoring && response.everyCategoryValid ? 'thumb-up' : 'thumb-down';
    }
})(document);
