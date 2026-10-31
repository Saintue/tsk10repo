def utils
def podYamlContent

pipeline {
    agent none

    stages {
        stage('Prepare') {
            agent {
                kubernetes {
                    yaml '''
apiVersion: v1
kind: Pod
spec:
  serviceAccountName: jenkins
  containers:
  - name: jnlp
    image: jenkins/inbound-agent:latest
    command: ['/busybox/cat']
    tty: true
'''
                    defaultContainer 'jnlp'
                }
            }
            steps {
                script {
                    checkout([$class: 'GitSCM',
                        branches: [[name: '*/main']],
                        userRemoteConfigs: [[url: 'https://github.com/Saintue/tsk10repo.git']]
                    ])
                    
                    podYamlContent = readFile('ci/kaniko-pod.yaml')
                    utils = load 'ci/ci-utils.groovy'
                    
                    stash name: 'source-code', includes: 'back-end/, front-end/, ci/'
                }
            }
        }

        stage('Build Applications') {
            parallel {
                stage('Back-end') {
                    agent {
                        kubernetes {
                            yaml podYamlContent
                            defaultContainer 'jnlp'
                        }
                    }
                    steps {
                        unstash 'source-code'
                        container('kaniko') {
                            script {
                                utils.buildAndPush('back-end', 'back-end')
                            }
                        }
                    }
                }
                stage('Front-end') {
                    agent {
                        kubernetes {
                            yaml podYamlContent
                            defaultContainer 'jnlp'
                        }
                    }
                    steps {
                        unstash 'source-code'
                        container('kaniko') {
                            script {
                                utils.buildAndPush('front-end', 'front-end')
                            }
                        }
                    }
                }
            }
        }
    }
}
