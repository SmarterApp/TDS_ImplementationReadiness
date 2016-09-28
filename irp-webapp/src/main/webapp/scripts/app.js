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
        app.$.btnManualMode.addEventListener('click', function (event) {
            that.$.pageModeSelection.select(1);
        });
        app.$.btnAutomatedMode.addEventListener('click', function (event) {
            that.$.pageModeSelection.select(2);
        });

        app.$.btnFileUpload.addEventListener('click', function (event) {
            if (that.$.clientName.value != null && that.$.clientName.value != '') {
                this.hidden = true;
                that.$.spinnerUpload.active = true;
            }
        });
        app.$.fileUploader.addEventListener('submitted', function (event) {
            that.$.btnFileUpload.hidden = false;
            that.$.spinnerUpload.active = false;
            if (event.detail.status == 200) {
                var analysisResponse = JSON.parse(event.detail.responseText);
                that.responses = analysisResponse;
            } else {
                that.$.fileUploadErrorDialog.toggle();
            }
        });

        // Automation Mode events
        function performAnalysis(tdsReportLinks) {
            console.info("Sending data to IRP Server for Analysis");
            //$.post( "/analysisReports", tdsReportsLinks );
        }
        window.addEventListener("message", function(event) {
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
        app.$.btnBeginAutomation.addEventListener('click', function (event) {
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

    // Main area's paper-scroll-header-panel custom condensing transformation of
    // the appName in the middle-container and the bottom title in the bottom-container.
    // The appName is moved to top and shrunk on condensing. The bottom sub title
    // is shrunk to nothing on condensing.
    window.addEventListener('paper-header-transform', function(e) {
        var appName = Polymer.dom(document).querySelector('#mainToolbar .app-name');
        var middleContainer = Polymer.dom(document).querySelector('#mainToolbar .middle-container');
        var bottomContainer = Polymer.dom(document).querySelector('#mainToolbar .bottom-container');
        var detail = e.detail;
        var heightDiff = detail.height - detail.condensedHeight;
        var yRatio = Math.min(1, detail.y / heightDiff);
        // appName max size when condensed. The smaller the number the smaller the condensed size.
        var maxMiddleScale = 0.50;
        var auxHeight = heightDiff - detail.y;
        var auxScale = heightDiff / (1 - maxMiddleScale);
        var scaleMiddle = Math.max(maxMiddleScale, auxHeight / auxScale + maxMiddleScale);
        var scaleBottom = 1 - yRatio;

        // Move/translate middleContainer
        Polymer.Base.transform('translate3d(0,' + yRatio * 100 + '%,0)', middleContainer);

        // Scale bottomContainer and bottom sub title to nothing and back
        Polymer.Base.transform('scale(' + scaleBottom + ') translateZ(0)', bottomContainer);

        // Scale middleContainer appName
        Polymer.Base.transform('scale(' + scaleMiddle + ') translateZ(0)', appName);
    });

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

    app.computeResponseIcon = function (response) {
        return response.validXMLfile && response.validTestName && response.validExaminee && response.validScoring ? 'thumb-up' : 'thumb-down';
    }
})(document);
