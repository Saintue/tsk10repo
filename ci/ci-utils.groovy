def buildAndPush(String appDir, String imageName) {
    // Берем полный хэш из Jenkins и оставляем первые 7 символов
    def shortSha = env.GIT_COMMIT.take(7) 
    
    sh """
    /kaniko/executor \
      --context ./${appDir} \
      --dockerfile ./${appDir}/Dockerfile \
      --destination harbor.local/java-app/${imageName}:${shortSha} \
      --registry-certificate harbor.local=/certs/ca.crt \
      --cache=true
    """
}

def deployHelm(String appName, String chartName) {
    def shortSha = env.GIT_COMMIT.take(7)
    
    sh """
    helm upgrade --install ${chartName} ./charts/${chartName}-chart \
      --namespace app \
      --rollback-on-failure \
      --timeout 5m \
      --set image.repository="harbor.local/java-app/${appName}" \
      --set image.tag="${shortSha}"
    """
}
return this
