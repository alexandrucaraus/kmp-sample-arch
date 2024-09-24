# kmp-sample-arch
Modular Kotlin Multiplatform Application Boilerplate


# Module Structure  

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
  :nativeApp --> :composeApp
  :composeApp --> :features:notes
  :composeApp --> :data:database
```


# Features
### Note Taking

| Android                                                                                                                    | iOS                                                                                                               |
|----------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------|
| ![Android App](https://raw.githubusercontent.com/alexandrucaraus/kmp-sample-arch/refs/heads/main/docs/images/android.gif)  | ![iOS App](https://raw.githubusercontent.com/alexandrucaraus/kmp-sample-arch/refs/heads/main/docs/images/ios.gif) |

# Dependencies

compose = "1.7.0-beta02"  
compose-material3 = "1.3.0"  
compose-navigation = "2.8.0-alpha10"    
kotlinx-coroutines = "1.9.0-RC.2"  
kotlinx-serialization = "1.7.1"   
koin = "4.0.0"  
room = "2.7.0-alpha08"

# Build steps

1. Download Android Studio Lady Bug (https://developer.android.com/studio/preview)

2. Install kotlin multiplatform plugin (https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform)
    - Settings -> Plugins -> Marketplace -> Kotlin Multiplatform

3. Clone repository and open in Android Studio

4. 
```bash
git clone https://github.com/alexandrucaraus/kmp-sample-arch.git
```

4. Run configuration either android or ios (needs to be on a mac).