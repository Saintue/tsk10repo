def utils
def buildPodYaml
def lintBackendPodYaml
def lintFrontendPodYaml
def helmPodYaml

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
    image: jenkins/inbound-agent:3391.va_37fa_a_305d6d-1-jdk25
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

                    buildPodYaml = readFile('ci/kaniko-pod.yaml')
                    lintBackendPodYaml = readFile('ci/lint-backend-pod.yaml')
                    lintFrontendPodYaml = readFile('ci/lint-frontend-pod.yaml')
                    helmPodYaml = readFile('ci/helm-pod.yaml')
                    utils = load 'ci/ci-utils.groovy'

                    stash name: 'source-code', includes: 'back-end/, front-end/, ci/, charts/'
                }
            }
        }

        stage('Lint Applications') {
            parallel {
                stage('Lint Back-end') {
                    agent {
                        kubernetes {
                            yaml lintBackendPodYaml
                            defaultContainer 'jnlp'
                        }
                    }
                    steps {
                        unstash 'source-code'
                        container('gradle-lint') {
                            catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                                sh 'cd back-end && gradle checkstyleMain --no-daemon'
                            }
                        }
                    }
                }
                stage('Lint Front-end') {
                    agent {
                        kubernetes {
                            yaml lintFrontendPodYaml
                            defaultContainer 'jnlp'
                        }
                    }
                    steps {
                        unstash 'source-code'
                        container('node-lint') {
                            sh 'cd front-end && npm ci && npm run lint'
                        }
                    }
                }
            }
        }

        stage('Build Applications') {
            parallel {
                stage('Back-end') {
                    agent {
                        kubernetes {
                            yaml buildPodYaml
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
                            yaml buildPodYaml
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

        stage('Deploy Applications') {
            parallel {
                stage('Deploy Back-end') {
                    agent {
                        kubernetes {
                            yaml helmPodYaml
                            defaultContainer 'jnlp'
                        }
                    }
                    steps {
                        unstash 'source-code'
                        container('helm') {
                            script {
                                utils.deployHelm('back-end', 'backend')
                            }
                        }
                    }
                }
                stage('Deploy Front-end') {
                    agent {
                        kubernetes {
                            yaml helmPodYaml
                            defaultContainer 'jnlp'
                        }
                    }
                    steps {
                        unstash 'source-code'
                        container('helm') {
                            script {
                                utils.deployHelm('front-end', 'frontend')
                            }
                        }
                    }
                }
            }
        }
    }
}
