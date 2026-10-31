def buildAndPush(String appDir, String imageName) {
    sh """
    /kaniko/executor \
      --context ./${appDir} \
      --dockerfile ./${appDir}/Dockerfile \
      --destination harbor.local/java-app/${imageName}:\${BUILD_ID} \
      --registry-certificate harbor.local=/certs/ca.crt \
      --cache=true
    """
}

def deployHelm(String appName, String chartName) {
    sh """
    helm upgrade --install ${chartName} ./charts/${chartName}-chart \
      --namespace app \
      --rollback-on-failure \
      --timeout 5m \
      --set image.repository="harbor.local/java-app/${appName}" \
      --set image.tag="\${BUILD_ID}"
    """
}
return this
