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
        sh './gradlew compileDebugSources'
      }
    }
    stage('Build APK') {
      steps {
        sh './gradlew assembleDebug'
        archiveArtifacts '**/*.apk'
      }
    }
  }
}
