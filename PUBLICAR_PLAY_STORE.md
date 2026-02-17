# 📱 Guia Completo: Publicar na Google Play Store

## 📋 Checklist de Pré-Publicação

### 1. ⚙️ Configurações do Projeto

#### a) Atualizar IDs do AdMob
📍 Arquivo: `app/src/main/res/values/strings.xml`

Substitua os IDs de teste pelos seus IDs reais do AdMob:

```xml
<!-- ANTES (IDs de teste) -->
<string name="admob_app_id">ca-app-pub-3940256099942544~3347511713</string>
<string name="banner_ad_unit_id">ca-app-pub-3940256099942544/6300978111</string>
<string name="interstitial_ad_unit_id">ca-app-pub-3940256099942544/1033173712</string>

<!-- DEPOIS (Seus IDs reais) -->
<string name="admob_app_id">ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX</string>
<string name="banner_ad_unit_id">ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX</string>
<string name="interstitial_ad_unit_id">ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX</string>
```

#### b) Verificar Versão
📍 Arquivo: `app/build.gradle`

```gradle
defaultConfig {
    applicationId "com.motivacional.frases"
    versionCode 1        // Incrementar a cada nova versão
    versionName "1.0.0"  // Versão visível para usuários
}
```

#### c) Firebase Configurado
- ✅ `google-services.json` no diretório `app/`
- ✅ Firestore ativado com frases cadastradas
- ✅ Firebase Cloud Messaging configurado
- ✅ Firebase Analytics ativo

### 2. 🔐 Criar Keystore (Assinatura Digital)

#### Gerar Keystore

```bash
keytool -genkey -v -keystore uplift-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias uplift
```

**Informações solicitadas:**
- Nome completo: [Seu nome ou empresa]
- Unidade organizacional: [Sua empresa ou "Independent"]
- Organização: [Nome da organização]
- Cidade: [Sua cidade]
- Estado: [Seu estado/UF]
- Código do país: BR

#### ⚠️ IMPORTANTE: Guardar com Segurança

Após criar, guarde em local seguro:
- ✅ O arquivo `.jks` (keystore)
- ✅ A senha do keystore
- ✅ A senha da chave (alias)
- ✅ O nome do alias

**SE PERDER ESSAS INFORMAÇÕES, NUNCA MAIS PODERÁ ATUALIZAR O APP NA PLAY STORE!**

#### Configurar no build.gradle

📍 Arquivo: `app/build.gradle`

Descomente e preencha:

```gradle
android {
    // ...

    signingConfigs {
        release {
            storeFile file("../uplift-release.jks")  // Caminho do keystore
            storePassword "SUA_SENHA_STORE"
            keyAlias "uplift"
            keyPassword "SUA_SENHA_KEY"
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
}
```

### 3. 🏗️ Gerar AAB/APK de Release

#### Opção A: Android Studio (Recomendado)

1. Build > Generate Signed Bundle / APK
2. Escolha "Android App Bundle" (AAB)
3. Selecione seu keystore
4. Escolha build variant: `release`
5. Clique em "Finish"

O arquivo será gerado em: `app/build/outputs/bundle/release/app-release.aab`

#### Opção B: Linha de Comando

```bash
# Gerar AAB (recomendado para Play Store)
./gradlew bundleRelease

# OU gerar APK
./gradlew assembleRelease
```

### 4. ✅ Testar o Build de Release

```bash
# Instalar APK de release no dispositivo
adb install app/build/outputs/apk/release/app-release.apk
```

**Checklist de testes:**
- [ ] App abre sem crashes
- [ ] Todas as telas funcionam
- [ ] Anúncios aparecem (use IDs reais de teste do AdMob)
- [ ] Firebase conecta e carrega frases
- [ ] Favoritos funcionam
- [ ] Compartilhamento funciona
- [ ] Notificações funcionam
- [ ] Modo escuro funciona
- [ ] Copiar frase funciona

## 🎨 Preparar Assets para Play Store

### 1. Ícone do App (512x512 px)
- Formato: PNG de 32 bits
- Tamanho: 512x512 pixels
- Sem transparência
- Pode ter cantos arredondados

### 2. Feature Graphic (1024x500 px)
- Banner promocional principal
- Formato: PNG ou JPG
- Tamanho: 1024x500 pixels
- Aparece no topo da listagem

### 3. Screenshots

**Telefone (obrigatório):**
- Mínimo: 2 screenshots
- Máximo: 8 screenshots
- Dimensões: Entre 320px e 3840px
- Proporção: 16:9 ou 9:16

**Tablet (opcional mas recomendado):**
- Mínimo: 2 screenshots
- Mesmas regras dos screenshots de telefone

**Sugestões de screenshots:**
1. Tela inicial com lista de frases
2. Card de frase em destaque
3. Frase do dia
4. Tela de favoritos
5. Filtro por categorias
6. Modo escuro
7. Notificação
8. Compartilhamento

### 4. Vídeo Promocional (opcional)
- YouTube
- 30 segundos a 2 minutos
- Demonstra principais funcionalidades

## 📝 Criar Conta no Google Play Console

### Passo a Passo

1. Acesse: https://play.google.com/console
2. Crie uma conta de desenvolvedor
3. Pague a taxa única de $25 USD
4. Complete seu perfil de desenvolvedor

## 🚀 Publicar o App

### 1. Criar Novo App

1. Play Console > "Criar app"
2. Preencher:
   - Nome: "Uplift - Frases Motivacionais"
   - Idioma padrão: Português (Brasil)
   - Tipo: App
   - Grátis ou pago: Grátis
   - Declarações obrigatórias (aceitar termos)

### 2. Configurar Listagem da Loja

#### Detalhes do App
- **Título**: Uplift - Frases Motivacionais
- **Descrição curta** (80 caracteres):
  ```
  Frases inspiradoras diárias com categorias, favoritos e compartilhamento
  ```

- **Descrição completa** (até 4000 caracteres):
  ```
  ✨ Uplift - Sua Dose Diária de Motivação

  Receba frases inspiradoras todos os dias e transforme sua rotina com mensagens positivas!

  🌟 PRINCIPAIS FUNCIONALIDADES:

  📚 CATEGORIAS VARIADAS
  • Motivação
  • Sucesso
  • Amor
  • Vida
  • Sabedoria

  ⭐ FAVORITOS
  Salve suas frases preferidas e acesse rapidamente

  📤 COMPARTILHE
  Envie frases para amigos via WhatsApp, Instagram, Facebook e muito mais

  🔔 NOTIFICAÇÕES DIÁRIAS
  Receba uma frase motivacional todo dia no horário que preferir

  🎯 FRASE DO DIA
  Destaque especial com a melhor frase do dia

  🌙 MODO ESCURO
  Interface moderna com tema claro e escuro automático

  💎 INTERFACE ELEGANTE
  Design moderno com Material Design 3, animações suaves e navegação intuitiva

  📋 COPIAR FÁCIL
  Copie qualquer frase com um toque para usar onde quiser

  ✅ GRÁTIS E SEM CADASTRO
  Baixe e comece a usar imediatamente

  🌍 SEMPRE ATUALIZADO
  Novas frases adicionadas regularmente

  Baixe agora e transforme seus dias com mensagens inspiradoras! 🚀
  ```

#### Recursos gráficos
- Upload do ícone (512x512)
- Upload do feature graphic (1024x500)
- Upload dos screenshots

### 3. Categorização

- **Categoria**: Estilo de vida
- **Tags**: frases, motivação, inspiração, citações, quotes

### 4. Informações de Contato

- Email: [seu-email-de-contato]
- Telefone (opcional)
- Site (opcional)

### 5. Política de Privacidade

Você PRECISA de uma política de privacidade. Opções:

**Opção A: Gerador Online Grátis**
- https://www.privacypolicygenerator.info/
- https://app-privacy-policy-generator.firebaseapp.com/

**Opção B: Template Básico**

Hospede no GitHub Pages ou em qualquer servidor e use a URL.

### 6. Classificação de Conteúdo

Responda ao questionário:
- **Violência**: Não
- **Conteúdo sexual**: Não
- **Linguagem obscena**: Não
- **Uso de drogas**: Não
- **Outros**: Não

Classificação esperada: **Livre (L)** ou **Todos os públicos**

### 7. Público-Alvo

- **Faixa etária**: Todos os públicos (ou 13+)
- **Países**: Brasil (ou mundial)

### 8. Preços e Distribuição

- **Preço**: Grátis
- **Países**: Selecione países desejados
- **Contém anúncios**: ✅ Sim (AdMob)
- **Compras no app**: ❌ Não

### 9. Conteúdo do App

#### Upload do AAB
1. Produção > Criar nova versão
2. Upload do arquivo: `app-release.aab`
3. Nome da versão: 1.0.0
4. Notas da versão:
   ```
   🎉 Lançamento inicial!

   ✨ Interface moderna e elegante
   📚 Categorias de frases
   ⭐ Sistema de favoritos
   📤 Compartilhamento social
   🔔 Notificações diárias
   🌙 Modo escuro
   ```

### 10. Revisar e Publicar

1. Complete todos os itens obrigatórios
2. Clique em "Revisar versão"
3. Verifique se não há erros
4. Clique em "Iniciar implantação para produção"

## ⏱️ Tempo de Análise

- **Primeira versão**: 3-7 dias
- **Atualizações**: 1-3 dias

## 📊 Após a Publicação

### 1. Monitorar
- Crashes no Firebase Crashlytics
- Estatísticas no Play Console
- Receita do AdMob

### 2. Responder Avaliações
- Responda feedback dos usuários
- Corrija bugs reportados

### 3. Atualizar Regularmente
- Adicione novas frases
- Corrija bugs
- Adicione novos recursos

## 💰 Monetização com AdMob

### Configurar AdMob

1. **Criar conta**: https://admob.google.com/
2. **Criar app** no AdMob
3. **Criar unidades de anúncio**:
   - Banner (320x50)
   - Intersticial (tela cheia)
4. **Copiar IDs** e colar em `strings.xml`

### Otimizar Receita

- **Ativar Mediação**: Maximize CPM com múltiplas redes
- **Testar Posicionamento**: Não exagere nos anúncios
- **Analisar Métricas**: eCPM, fill rate, CTR

## 🐛 Solução de Problemas Comuns

### App rejeitado por violação de política
- Verifique política de privacidade
- Verifique permissões solicitadas
- Verifique conteúdo das frases

### AAB não é aceito
- Verifique assinatura digital
- Verifique versionCode único
- Limpe projeto e recompile

### Anúncios não aparecem
- Aguarde até 24h após ativar IDs
- Verifique IDs copiados corretamente
- Teste com IDs de teste primeiro

## ✅ Checklist Final

- [ ] IDs do AdMob atualizados
- [ ] Firebase configurado
- [ ] Keystore criado e guardado
- [ ] AAB gerado e testado
- [ ] Ícone 512x512 criado
- [ ] Feature graphic 1024x500 criado
- [ ] Screenshots tirados (mín 2)
- [ ] Descrições escritas
- [ ] Política de privacidade criada
- [ ] Conta Play Console criada
- [ ] Taxa de $25 paga
- [ ] App criado no console
- [ ] Classificação preenchida
- [ ] AAB enviado
- [ ] Versão revisada e enviada

## 🎉 Parabéns!

Agora é só aguardar a aprovação do Google. Boa sorte! 🚀

---

**Dúvidas?** Consulte a documentação oficial: https://developer.android.com/distribute
