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
    stage('Build') {
      steps {
        container('maven') {
          sh 'mvn clean package sonar:sonar'
          sh 'ls -la'
        }
      }
    }
    stage('Test ') {
      steps {
        container('maven') {
          sh 'mvn test'
          sh 'ls -la'
        }
      }
    }
  }
}

