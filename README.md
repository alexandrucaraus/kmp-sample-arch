# kmp-sample-arch
Modular Kotlin Multiplatform Application Boilerplate


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