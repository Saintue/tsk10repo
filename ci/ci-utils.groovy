def buildAndPush(String appDir, String imageName) {
    sh """
    /kaniko/executor \
      --context \$(pwd)/${appDir} \
      --dockerfile \$(pwd)/${appDir}/Dockerfile \
      --destination harbor.local/java-app/${imageName}:\${BUILD_ID} \
      --registry-certificate harbor.local=/certs/ca.crt \
      --cache=true
    """
}
return this
