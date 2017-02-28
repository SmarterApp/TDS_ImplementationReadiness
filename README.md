[ ![Codeship Status for sbcresst/IReadinessPackage](https://codeship.com/projects/0d0b1a20-6080-0132-c347-02eb9615503b/status)](https://codeship.com/projects/51655)

# Overview #
This is the source repository for the Implementation Readiness Package (IRP) developed by UCLA CRESST as part of the Smarter Balance initiative.
IRP can be used by vendors to check their custom Assessment Delivery Systems implementation.

For more information visit http://www.smarterapp.org.

### What is this repository for? ###

* Implementation Readiness Package
* Version: 2.0.0

### Technology Used ###
* Maven
* Spring Framework
* Polymer
* Thymeleaf

### How do I get set up? ###

* Install node/npm (http://nodejs.org/download/)
* Install bower (http://bower.io/)
* Install bower-installer (https://github.com/blittle/bower-installer)
* Install Java 7
* Install Maven >= 3.2

### How do I build? ###
* To build: `mvn clean install`
* To run tomcat 7: `mvn tomcat7:run`

### Deployment ###
IRP is built by CodeShip and hosted on AWS Elastic Beanstalk. Two Elastic Beanstalk instances exist:
Production and Staging.  The `deploy_beanstalk.sh` script is executed by CodeShip after a successful build of `master`.
That script deploys the successfully built WAR file.  The built WAR is deployed to the Staging Elastic Beanstalk instance.

### Contribution guidelines ###

* Write tests - Acceptance Tests using Cucumber and drive your design with TDD
* Code review

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact
