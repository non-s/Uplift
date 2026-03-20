# ⚡ Início Rápido

## 🎯 Passos Essenciais (15 minutos)

### 1. Instalar Android Studio
- Baixe em: https://developer.android.com/studio
- Instale e configure o SDK Android

### 2. Configurar Firebase (5 min)
1. Acesse: https://console.firebase.google.com/
2. Crie novo projeto "Frases Motivacionais"
3. Adicione app Android:
   - Pacote: `com.motivacional.frases`
4. Baixe `google-services.json`
5. Cole na pasta `app/` do projeto

### 3. Ativar Firestore (2 min)
1. No Firebase Console → Firestore Database
2. Criar banco de dados → Modo de produção
3. Localização: southamerica-east1

### 4. Abrir Projeto (3 min)
1. Android Studio → Open → Selecione pasta "FrasesMotivacionais"
2. Aguarde sincronização do Gradle
3. Conecte celular ou inicie emulador

### 5. Executar App (5 min)
1. Clique em Run (▶️)
2. No app, clique nos 3 pontos → "Inicializar Database"
3. Pronto! O app está funcionando

## 💰 Para Monetizar

### Configurar AdMob (10 min)
1. Acesse: https://apps.admob.com/
2. Adicionar app "Frases Motivacionais"
3. Criar 2 blocos de anúncios:
   - Banner
   - Intersticial
4. Substituir IDs em:
   - `AndroidManifest.xml` (APPLICATION_ID)
   - `strings.xml` (banner_ad_unit_id e interstitial_ad_unit_id)

## 📱 Para Publicar

Veja o guia completo em `PUBLICAR.md`

---

**Dúvidas?** Leia `README.md` para documentação completa
