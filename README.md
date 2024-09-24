# kmp-sample-arch
Modular Kotlin Multiplatform Application Boilerplate

### Module Structure  

```mermaid
%%{
  init: {
    'theme': 'base',
    'themeVariables': {"primaryTextColor":"#fff","primaryColor":"#5a4f7c","primaryBorderColor":"#5a4f7c","lineColor":"#f5a623","tertiaryColor":"#40375c","fontSize":"12px"}
  }
}%%

graph LR
  subgraph :data
    :data:database["database"]
  end
  subgraph :features
    :features:notes["notes"]
  end
  :androidApp(iosApp) --> :composeApp
  :composeApp --> :features:notes
  :composeApp --> :data:database
```


# Features
### Note Taking

| Android                                                                                                                    | iOS                                                                                                               |
|----------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------|
| ![Android App](https://raw.githubusercontent.com/alexandrucaraus/kmp-sample-arch/refs/heads/main/docs/images/android.gif)  | ![iOS App](https://raw.githubusercontent.com/alexandrucaraus/kmp-sample-arch/refs/heads/main/docs/images/ios.gif) |


# Build steps
### 