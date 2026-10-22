# DecifraApp

Aplicativo Android desenvolvido em **Kotlin** com **Jetpack Compose**. É um jogo onde você escolhe um tema e tenta decifrar as respostas.

## Tecnologias

- Kotlin
- Jetpack Compose (Material 3)
- Navigation Compose
- Kotlinx Serialization
- Gradle (Kotlin DSL) com Gradle Wrapper

## Requisitos

- [Android Studio](https://developer.android.com/studio) (versão recente)
- JDK 11 ou superior
- Android SDK com `compileSdk 37` instalado

Não é necessário instalar o Gradle manualmente, o projeto já inclui o Gradle Wrapper.

## Como rodar o projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/<seu-usuario>/<seu-repositorio>.git
   ```

2. Abra a pasta do projeto no **Android Studio** (`File > Open`).

3. Aguarde o Android Studio sincronizar o Gradle e baixar as dependências.

4. O arquivo `local.properties` é gerado automaticamente pelo Android Studio
   apontando para o SDK da sua máquina. Ele **não** vem no repositório de
   propósito (é específico de cada computador). Se por algum motivo não for
   gerado, crie um `local.properties` na raiz com:
   ```properties
   sdk.dir=C:\\Caminho\\Para\\Seu\\Android\\Sdk
   ```

5. Selecione um emulador ou conecte um dispositivo físico e clique em **Run** (▶).

## Rodar pela linha de comando

Windows:
```bash
gradlew.bat assembleDebug
```

Linux / macOS:
```bash
./gradlew assembleDebug
```

O APK de debug fica em `app/build/outputs/apk/debug/`.

## Estrutura do projeto

```
app/src/main/java/com/example/decifraappescolhaumtema/
├── auth/            # Gerenciamento de autenticação
├── Navigation/      # Rotas e navegação entre telas
├── ui/theme/        # Tema, cores e tipografia
├── MainActivity.kt  # Ponto de entrada
└── tela*.kt         # Telas do app (Login, Criar Conta, Jogar, etc.)
```

## Configuração

- `applicationId`: `com.example.decifraappescolhaumtema`
- `minSdk`: 24
- `targetSdk` / `compileSdk`: 37
