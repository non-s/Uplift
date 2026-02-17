# 📱 Frases Motivacionais

> Aplicativo Android de frases motivacionais diárias com notificações, temas personalizáveis e sistema de favoritos.

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4.svg)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 🌟 Sobre o Projeto

**Frases Motivacionais** é um aplicativo Android nativo desenvolvido em Kotlin com Jetpack Compose que oferece milhares de frases inspiradoras organizadas por categorias, com notificações diárias personalizáveis, sistema de favoritos e interface moderna e intuitiva.

### ✨ Principais Recursos

- 📖 **5 Categorias**: Motivação, Sucesso, Amor, Vida, Sabedoria
- ⭐ **Sistema de Favoritos**: Salve suas frases preferidas
- 🔔 **Notificações Diárias**: Receba inspiração no horário escolhido
- 🎨 **Temas Personalizáveis**: Modo claro/escuro + 5 temas de cores
- 📝 **Tamanhos de Fonte**: 3 opções ajustáveis (Pequeno, Médio, Grande)
- 📤 **Compartilhamento**: Compartilhe frases em redes sociais
- 📋 **Copiar Texto**: Copie frases para a área de transferência
- 🎯 **Widget**: Frase do dia na tela inicial (em desenvolvimento)
- 🔍 **Busca**: Encontre frases por texto ou autor (em desenvolvimento)

---

## 🏗️ Arquitetura

O projeto utiliza arquitetura **MVVM (Model-View-ViewModel)** com as melhores práticas do Android moderno:

```
app/
├── data/
│   ├── model/          # Modelos de dados (Quote, Category)
│   ├── local/          # Room Database (entidades, DAOs)
│   ├── repository/     # Camada de repositório
│   └── QuotesData.kt   # Dados estáticos de frases
├── ui/
│   ├── MainActivity.kt          # Tela principal
│   ├── SettingsActivity.kt      # Tela de configurações
│   ├── SearchActivity.kt        # Tela de busca
│   ├── adapters/                # Adaptadores RecyclerView
│   ├── viewmodel/               # ViewModels (Quote, Theme)
│   ├── theme/                   # Sistema de temas Material 3
│   └── widget/                  # Widget da tela inicial
├── utils/
│   ├── DailyQuoteWorker.kt      # WorkManager para notificações
│   ├── PreferencesManager.kt    # DataStore Preferences
│   ├── ThemeHelper.kt           # Gerenciamento de temas
│   ├── ShareHelper.kt           # Compartilhamento
│   └── QuoteImageGenerator.kt   # Geração de imagens
└── proguard-rules.pro           # Regras de ofuscação
```

---

## 🛠️ Tecnologias Utilizadas

### Core
- **Kotlin 2.0.0** - Linguagem de programação
- **Jetpack Compose 1.7.6** - UI moderna e declarativa
- **Material Design 3** - Design system do Google

### Jetpack Libraries
- **Room 2.8.3** - Banco de dados local
- **ViewModel & LiveData** - Gerenciamento de estado
- **WorkManager 2.11.0** - Tarefas em background
- **DataStore 1.0.0** - Armazenamento de preferências

### Firebase
- **Firestore** - Banco de dados em nuvem
- **Analytics** - Análise de uso
- **Authentication** - Autenticação de usuários
- **Cloud Messaging** - Notificações push

### Outros
- **Google AdMob** - Monetização com anúncios
- **Kotlin Coroutines** - Programação assíncrona
- **Coil** - Carregamento de imagens

---

## 📋 Requisitos

- **Android Studio**: Hedgehog (2023.1.1) ou superior
- **Gradle**: 8.7.0
- **Kotlin**: 2.0.0
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 36

---

## 🚀 Como Executar

### 1. Clone o repositório
```bash
git clone https://github.com/SEU-USUARIO/FrasesMotivacionais.git
cd FrasesMotivacionais
```

### 2. Configure Firebase (Opcional)
1. Crie um projeto no [Firebase Console](https://console.firebase.google.com)
2. Baixe o arquivo `google-services.json`
3. Coloque em `app/google-services.json`

### 3. Configure AdMob (Opcional)
Edite `app/src/main/AndroidManifest.xml`:
```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-SEU_ID_AQUI~SEU_APP_ID"/>
```

### 4. Compile e Execute
```bash
# Windows
gradlew.bat clean
gradlew.bat installDebug

# Linux/Mac
./gradlew clean
./gradlew installDebug
```

---

## 📦 Build de Release

### 1. Gerar Keystore
```bash
keytool -genkeypair -v -keystore meu-app-key.keystore -alias meu-app -keyalg RSA -keysize 2048 -validity 10000
```

### 2. Configurar Signing
Edite `app/build.gradle`:
```gradle
signingConfigs {
    release {
        storeFile file("../meu-app-key.keystore")
        storePassword "SUA_SENHA"
        keyAlias "meu-app"
        keyPassword "SUA_SENHA"
    }
}

buildTypes {
    release {
        signingConfig signingConfigs.release
        minifyEnabled true
        shrinkResources true
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
}
```

### 3. Gerar AAB
```bash
gradlew bundleRelease
```

Arquivo gerado: `app/build/outputs/bundle/release/app-release.aab`

---

## 📱 Funcionalidades Detalhadas

### Tela Principal
- Lista de frases por categoria
- Filtro por categoria (Todas, Motivação, Sucesso, etc.)
- Botão de favoritar
- Compartilhamento
- Copiar para área de transferência
- Frase do dia destacada

### Configurações
- **Aparência**:
  - Modo Escuro (Sistema/Claro/Escuro)
  - Tema de Cores (Roxo/Azul/Verde/Vermelho/Laranja)
  - Tamanho de Fonte (Pequeno/Médio/Grande)
- **Notificações**:
  - Ativar/Desativar notificações diárias
  - Configurar horário
- **Sobre**: Informações do app e versão

### Sistema de Notificações
- **WorkManager** para agendamento confiável
- Notificação diária no horário escolhido
- Frase aleatória a cada dia
- Cancela automaticamente ao desativar

---

## 🎨 Sistema de Temas

### Cores Disponíveis
- **Roxo** (Padrão): Primary #6200EE, Secondary #03DAC5
- **Azul**: Primary #1976D2, Secondary #64B5F6
- **Verde**: Primary #388E3C, Secondary #81C784
- **Vermelho**: Primary #D32F2F, Secondary #FF6659
- **Laranja**: Primary #F57C00, Secondary #FFB74D

### Modo Escuro
- Suporte completo a Material 3 Dynamic Colors
- Tema escuro otimizado para AMOLED
- Transição suave entre modos

---

## 📄 Arquivos de Documentação

- **[PRONTO_PARA_PUBLICAR.md](PRONTO_PARA_PUBLICAR.md)** - Checklist de publicação e status atual
- **[GUIA_PUBLICACAO_COMPLETO.md](GUIA_PUBLICACAO_COMPLETO.md)** - Guia detalhado passo a passo para publicar na Play Store
- **[POLITICA_PRIVACIDADE.md](POLITICA_PRIVACIDADE.md)** - Política de privacidade completa (LGPD/GDPR/COPPA)

---

## 🔒 Privacidade e Segurança

Este aplicativo está em conformidade com:
- ✅ **LGPD** (Lei Geral de Proteção de Dados - Brasil)
- ✅ **GDPR** (General Data Protection Regulation - UE)
- ✅ **COPPA** (Children's Online Privacy Protection Act - EUA)

### Dados Coletados
- Preferências do app (tema, fonte, notificações)
- Frases favoritas (armazenadas localmente)
- Dados anônimos de uso (Firebase Analytics)
- ID de publicidade (AdMob - pode ser desativado)

### Não Coletamos
- ❌ Informações pessoais identificáveis
- ❌ Contatos
- ❌ Localização precisa
- ❌ Fotos ou arquivos

Leia a [Política de Privacidade completa](POLITICA_PRIVACIDADE.md) para mais detalhes.

---

## 🐛 Problemas Conhecidos

### Resolvidos ✅
- [x] Crash ao abrir Configurações (Compose ripple incompatibility)
- [x] Crash ao selecionar tema de cores (Modifier.selectable issue)
- [x] Crash ao selecionar tamanho de fonte (Modifier.selectable issue)
- [x] TargetSdk 36 causando erro de receiver registration
- [x] Imports duplicados em MainActivity.kt

### Em Desenvolvimento 🚧
- [ ] Widget não está sendo atualizado automaticamente
- [ ] SearchActivity precisa de implementação da busca
- [ ] QuoteImageGenerator precisa de integração completa

---

## 📊 Status do Projeto

### ✅ Completo (80%)
- Arquitetura MVVM
- UI com Jetpack Compose
- Sistema de favoritos
- Notificações diárias
- Temas personalizáveis
- Compartilhamento
- Ícone profissional (Vector XML)
- Política de Privacidade
- Guia de Publicação
- ProGuard otimizado

### 🚧 Pendente (20%)
- Testes extensivos em diferentes dispositivos
- Keystore e assinatura de release
- Hospedagem da política de privacidade
- Assets da Play Store (Feature Graphic, Screenshots)
- Criação da conta Google Play Developer
- Upload na Play Store

---

## 🤝 Como Contribuir

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👤 Autor

**Desenvolvedor: Julio**

📧 E-mail: privacidade.frasesmotivacionais@gmail.com

---

## 🙏 Agradecimentos

- [Material Design](https://m3.material.io/) - Design system
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - UI toolkit
- [Firebase](https://firebase.google.com/) - Backend services
- [AdMob](https://admob.google.com/) - Monetização

---

## 📈 Roadmap Futuro

### Versão 1.1.0
- [ ] Busca de frases por texto/autor
- [ ] Widget funcional com atualização automática
- [ ] Mais categorias (Amizade, Gratidão, Coragem)
- [ ] Exportar favoritos para PDF/imagem

### Versão 1.2.0
- [ ] Sincronização em nuvem (Firebase)
- [ ] Autenticação de usuários
- [ ] Compartilhamento de coleções de favoritos
- [ ] Estatísticas de uso

### Versão 2.0.0
- [ ] Frases em áudio (Text-to-Speech)
- [ ] Modo offline completo
- [ ] Suporte a múltiplos idiomas
- [ ] Wallpaper animado com frases

---

## 🎯 Métricas de Qualidade

- **Cobertura de testes**: Em desenvolvimento
- **Crash rate**: < 1% (meta)
- **Tamanho do APK**: ~12 MB
- **Tamanho do AAB**: ~10 MB
- **Min API Level**: 24 (97% de dispositivos Android)

---

## 🔗 Links Úteis

- [Documentação Android](https://developer.android.com/docs)
- [Jetpack Compose Docs](https://developer.android.com/jetpack/compose/documentation)
- [Material Design 3](https://m3.material.io/)
- [Firebase Docs](https://firebase.google.com/docs)
- [Play Console Help](https://support.google.com/googleplay/android-developer)

---

**⭐ Se este projeto foi útil para você, considere dar uma estrela no GitHub!**

**🚀 Pronto para inspirar milhões de pessoas? Vamos publicar!**

---

*Última atualização: 18/11/2024 | Versão: 1.0.0*
