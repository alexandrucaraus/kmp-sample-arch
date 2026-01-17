# kmp-sample-arch
Modular Kotlin Multiplatform Application Boilerplate

### TODOs

1. Test
 - Finish test setup for snapshot testing when paparazzi 2.0.0-alpha04 is ready
 - More tests in common to cover the great part of code

2. Test Coverage
  - Add kover for the common code report 
  - Jacoco for android already done.

3. CI/CD
   - Linters
   - Coverage display android and common separate
   - Build documentation
   - Build artifacts
   - Publish
   - Manage testing on CI/CD

### Modules Structure  

```mermaid
%%{
  init: {
    'theme': 'base',
    'themeVariables': {"primaryTextColor":"#fff","primaryColor":"#5a4f7c","primaryBorderColor":"#5a4f7c","lineColor":"#f5a623","tertiaryColor":"#40375c","fontSize":"12px"}
  }
}%%

graph LR
  :androidApp["androidApp"]
  :app["app"]

  :androidApp --> :app

classDef android-application fill:#2C4162,stroke:#fff,stroke-width:2px,color:#fff;
classDef unknown fill:#676767,stroke:#fff,stroke-width:2px,color:#fff;
class :androidApp android-application
class :app unknown

```
# Features
### Note-Taking

| Android                                                                                                                    | iOS                                                                                                               |
|----------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------|
| ![Android App](https://raw.githubusercontent.com/alexandrucaraus/kmp-sample-arch/refs/heads/main/docs/images/android.gif)  | ![iOS App](https://raw.githubusercontent.com/alexandrucaraus/kmp-sample-arch/refs/heads/main/docs/images/ios.gif) |