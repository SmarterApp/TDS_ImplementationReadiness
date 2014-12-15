Feature: A TDS Report is uploaded and an analysis report is given.

  As a Assessment Delivery System Vendor
  I want to upload TDS Reports that my system generated
  So I can be assured that my system adheres to Smarter Balance standards

  Scenario: TDS Report XML is malformed
    Given I have a malformed TDS Report XML document
    When I upload the document
    Then The analysis report should indicate my TDS Report is malformed

  Scenario: TDS Report XML is well formed
    Given I have a valid TDS Report XML document
    When I upload the document
    Then The analysis report should indicate my TDS Report is valid

  Scenario: Multiple TDS Report Upload via ZIP file
    Given I have a ZIP file containing 3 TDS Report XML documents
    When I upload the document
    Then The analysis report should indicate that 3 TDS Reports have been uploaded

  Scenario: Upload file error
    Given I have a file to upload
    When I upload the file as an unknown file type
    Then The result of the upload should give me an error response