#!/bin/sh

export APP_VERSION=`git rev-parse --short HEAD`
pip install awscli

# clean build artifacts and create the application archive (also ignore any files named .git* in any folder)
git clean -fd

# precompile assets, ...

# move and rename the application to a versioned WAR file
mv irp-webapp/target/IReadinessPackage.war  "${APP_NAME}-${APP_VERSION}.war"

# delete any version with the same name (based on the short revision)
aws elasticbeanstalk delete-application-version --application-name "${APP_NAME}" --version-label "${APP_VERSION}"  --delete-source-bundle

# upload to S3
aws s3 cp ${APP_NAME}-${APP_VERSION}.war s3://${S3_BUCKET}/${APP_NAME}-${APP_VERSION}.war

# create a new version and update the environment to use this version
aws elasticbeanstalk create-application-version --application-name "${APP_NAME}" --version-label "${APP_VERSION}" --source-bundle S3Bucket="${S3_BUCKET}",S3Key="${APP_NAME}-${APP_VERSION}.war"
aws elasticbeanstalk update-environment --environment-name "${ENV_NAME}" --version-label "${APP_VERSION}"
