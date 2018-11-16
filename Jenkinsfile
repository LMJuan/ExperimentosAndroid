pipeline {
  agent {
    label 'android'
  }
  options {
    skipStagesAfterUnstable()
  }
  stages{
    stage('Compile') {
      steps {
        .sh './gradlew compileDebugSources'
      }
    }
  }
}
