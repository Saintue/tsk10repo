pipeline {
    agent none

    stages {
        stage('Build Applications') {
            parallel {
                stage('Back-end') {
                    agent {
                        kubernetes {
                            inheritFrom 'kaniko-builder'
                            defaultContainer 'jnlp'
                        }
                    }
                    stages {
                        stage('Checkout Back-end') {
                            steps {
                                checkout([$class: 'GitSCM',
                                    branches: [[name: '*/main']],
                                    extensions: [[$class: 'SparseCheckoutPaths',
                                                  sparseCheckoutPaths: [[$class: 'SparseCheckoutPath', path: 'back-end/']]]],
                                    userRemoteConfigs: [[url: 'https://github.com/Saintue/tsk10repo.git']]
                                ])
                            }
                        }
                        stage('Build & Push Back-end') {
                            steps {
                                container('kaniko') {
                                    sh '''
                                    /kaniko/executor \
                                      --context `pwd`/back-end \
                                      --dockerfile `pwd`/back-end/Dockerfile \
                                      --destination harbor.local/library/back-end:${BUILD_ID} \
                                      --cache=true
                                    '''
                                }
                            }
                        }
                    }
                }

                stage('Front-end') {
                    agent {
                        kubernetes {
                            inheritFrom 'kaniko-builder'
                            defaultContainer 'jnlp'
                        }
                    }
                    stages {
                        stage('Checkout Front-end') {
                            steps {
                                checkout([$class: 'GitSCM',
                                    branches: [[name: '*/main']],
                                    extensions: [[$class: 'SparseCheckoutPaths',
                                                  sparseCheckoutPaths: [[$class: 'SparseCheckoutPath', path: 'front-end/']]]],
                                    userRemoteConfigs: [[url: 'https://github.com/Saintue/tsk10repo.git']]
                                ])
                            }
                        }
                        stage('Build & Push Front-end') {
                            steps {
                                container('kaniko') {
                                    sh '''
                                    /kaniko/executor \
                                      --context `pwd`/front-end \
                                      --dockerfile `pwd`/front-end/Dockerfile \
                                      --destination harbor.local/library/front-end:${BUILD_ID} \
                                      --cache=true
                                    '''
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
