pipeline {
  agent {
    kubernetes {
      yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    some-label: some-label-value
spec:
  containers:
  - name: maven
    image: maven:alpine
    command:
    - cat
    tty: true
"""
    }
  }
  stages {
    stage('Run maven') {
      steps {
        container('maven') {
          //sh 'mvn clean package'
          sh 'ls -la'
      
      }
    }
    stage('Build ') {
      steps {
        container('maven') {
          sh 'cd sonarqube'
          sh 'ls -la'
        }
      }
    }
  }
}

