const SUPABASE_URL      = 'https://bvquyfzllqnbfxncsacn.supabase.co';
const SUPABASE_ANON_KEY = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImJ2cXV5ZnpsbHFuYmZ4bmNzYWNuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzgxODU1MzQsImV4cCI6MjA5Mzc2MTUzNH0.xa_rs4bVLoTv58P7U8rDOaPjo1Dqt60q8cR-IWFpbug';
const sb = supabase.createClient(SUPABASE_URL, SUPABASE_ANON_KEY);

const QUOTES = [
    { text:"A única forma de fazer um ótimo trabalho é amar o que você faz.", author:"Steve Jobs", cat:"motivacao" },
    { text:"Você não precisa ser grande para começar, mas precisa começar para ser grande.", author:"Zig Ziglar", cat:"motivacao" },
    { text:"A perseverança é o caminho do êxito.", author:"Charles Chaplin", cat:"motivacao" },
    { text:"Não espere. O momento certo nunca chegará.", author:"Napoleon Hill", cat:"motivacao" },
    { text:"Comece de onde você está. Use o que você tem. Faça o que puder.", author:"Arthur Ashe", cat:"motivacao" },
    { text:"A ação é a chave fundamental para todo sucesso.", author:"Pablo Picasso", cat:"motivacao" },
    { text:"Os sonhos não funcionam a menos que você trabalhe.", author:"John C. Maxwell", cat:"motivacao" },
    { text:"O segredo para avançar é começar.", author:"Mark Twain", cat:"motivacao" },
    { text:"Você é mais corajoso do que acredita e mais forte do que parece.", author:"A. A. Milne", cat:"motivacao" },
    { text:"A diferença entre o impossível e o possível está na determinação.", author:"Tommy Lasorda", cat:"motivacao" },
    { text:"Não desista. O começo é sempre o mais difícil.", author:"Desconhecido", cat:"motivacao" },
    { text:"Cada dia é uma nova oportunidade de mudar a sua vida.", author:"Desconhecido", cat:"motivacao" },
    { text:"O sucesso é a soma de pequenos esforços repetidos dia após dia.", author:"Robert Collier", cat:"sucesso" },
    { text:"O sucesso geralmente vem para quem está ocupado demais para procuá-lo.", author:"Henry David Thoreau", cat:"sucesso" },
    { text:"Não meça o sucesso pelo que você conquistou, mas pelos obstáculos que superou.", author:"Booker T. Washington", cat:"sucesso" },
    { text:"Sucesso é ir de fracasso em fracasso sem perder o entusiasmo.", author:"Winston Churchill", cat:"sucesso" },
    { text:"A diferença entre o comum e o extraordinário é esse pequeno extra.", author:"Jimmy Johnson", cat:"sucesso" },
    { text:"Vencedores nunca desistem e quem desiste nunca vence.", author:"Vince Lombardi", cat:"sucesso" },
    { text:"Para ter sucesso, primeiro você precisa acreditar que é possível.", author:"Nikos Kazantzakis", cat:"sucesso" },
    { text:"O fracasso é a oportunidade de começar de novo, desta vez de forma mais inteligente.", author:"Henry Ford", cat:"sucesso" },
    { text:"Trabalhe enquanto eles dormem, aprenda enquanto eles festejam.", author:"Desconhecido", cat:"sucesso" },
    { text:"O amor não consiste em olhar um para o outro, mas em olhar juntos na mesma direção.", author:"Antoine de Saint-Exupéry", cat:"amor" },
    { text:"Amar é encontrar a própria felicidade na felicidade do outro.", author:"Gottfried Leibniz", cat:"amor" },
    { text:"O amor é a única coisa que cresce quando é compartilhada.", author:"Antoine de Saint-Exupéry", cat:"amor" },
    { text:"A melhor prova de amor é a confiança.", author:"Joyce Brothers", cat:"amor" },
    { text:"Onde há amor, há vida.", author:"Mahatma Gandhi", cat:"amor" },
    { text:"O amor cura as pessoas — tanto quem o oferece quanto quem o recebe.", author:"Karl Menninger", cat:"amor" },
    { text:"A vida é o que acontece enquanto você está ocupado fazendo outros planos.", author:"John Lennon", cat:"vida" },
    { text:"No meio de cada dificuldade existe uma oportunidade.", author:"Albert Einstein", cat:"vida" },
    { text:"Viva como se fosse morrer amanhã. Aprenda como se fosse viver para sempre.", author:"Mahatma Gandhi", cat:"vida" },
    { text:"A vida é curta demais para ser pequena.", author:"Benjamin Disraeli", cat:"vida" },
    { text:"Não chore porque acabou. Sorria porque aconteceu.", author:"Gabriel García Márquez", cat:"vida" },
    { text:"O que não te mata te fortalece.", author:"Friedrich Nietzsche", cat:"vida" },
    { text:"Seja a mudança que você quer ver no mundo.", author:"Mahatma Gandhi", cat:"vida" },
    { text:"Aproveite cada momento. O tempo é o bem mais precioso.", author:"Desconhecido", cat:"vida" },
    { text:"Conhece-te a ti mesmo.", author:"Sócrates", cat:"sabedoria" },
    { text:"Nossa maior glória não está em nunca cair, mas em nos levantar cada vez que caímos.", author:"Confúcio", cat:"sabedoria" },
    { text:"A sabedoria começa quando reconhecemos o que não sabemos.", author:"Sócrates", cat:"sabedoria" },
    { text:"A educação é a arma mais poderosa que você pode usar para mudar o mundo.", author:"Nelson Mandela", cat:"sabedoria" },
    { text:"Um investimento em conhecimento sempre paga os melhores juros.", author:"Benjamin Franklin", cat:"sabedoria" },
    { text:"A dúvida é o princípio da sabedoria.", author:"Aristóteles", cat:"sabedoria" },
];

const CAT_LABELS = { all:"Todas", motivacao:"Motivação", sucesso:"Sucesso", amor:"Amor", vida:"Vida", sabedoria:"Sabedoria" };

const state = {
    cat: "all", idx: 0, filtered: [],
    favs: [],
    theme: localStorage.getItem("uplift_theme") || "purple",
    favOpen: false,
    userId: null,
};

/* ─── Algoritmo determinístico — mesma frase do dia para todos ─── */
function dateHash(date) {
    const s = date.toISOString().split('T')[0];
    let h = 0;
    for (let i = 0; i < s.length; i++) h = Math.imul(31, h) + s.charCodeAt(i) | 0;
    return Math.abs(h);
}

function seededShuffle(arr, seed) {
    const a = [...arr];
    let s = seed;
    for (let i = a.length - 1; i > 0; i--) {
        s = (s * 1664525 + 1013904223) & 0xffffffff;
        const j = Math.abs(s) % (i + 1);
        [a[i], a[j]] = [a[j], a[i]];
    }
    return a;
}

const getFiltered   = () => state.cat === "all" ? QUOTES : QUOTES.filter(q => q.cat === state.cat);
const buildFiltered = () => { state.filtered = seededShuffle(getFiltered(), dateHash(new Date())); state.idx = 0; };

/* ─── Autenticação anônima — sem login, mas identidade persistente ─── */
async function initAuth() {
    const { data: { session } } = await sb.auth.getSession();
    if (session) {
        state.userId = session.user.id;
    } else {
        const { data } = await sb.auth.signInAnonymously();
        state.userId = data.user?.id;
    }
    await loadApprovedSubmissions();
    if (state.userId) await syncFavs();
}

async function loadApprovedSubmissions() {
    const { data } = await sb.from('quote_submissions')
        .select('text, author, cat')
        .eq('approved', true);
    if (!data?.length) return;
    data.forEach(s => {
        if (!QUOTES.some(q => q.text === s.text)) {
            QUOTES.push({ text: s.text, author: s.author || 'Desconhecido', cat: s.cat || 'motivacao' });
        }
    });
}

/* ─── Favoritos (banco de dados) ─── */
async function syncFavs() {
    const { data } = await sb.from('favorites')
        .select('quote_text, quote_author, quote_cat')
        .order('created_at', { ascending: false });
    state.favs = (data || []).map(r => ({ text: r.quote_text, author: r.quote_author, cat: r.quote_cat }));
    document.getElementById('favCount').textContent = state.favs.length;
    if (state.favOpen) renderFavList();
    renderQuote();
}

async function toggleFav(quote) {
    const isFav = state.favs.some(f => f.text === quote.text);
    if (isFav) {
        await sb.from('favorites').delete().eq('quote_text', quote.text);
    } else {
        await sb.from('favorites').insert({
            quote_text: quote.text, quote_author: quote.author, quote_cat: quote.cat,
        });
    }
    await syncFavs();
}

async function clearAllFavs() {
    if (!confirm('Remover todos os favoritos?')) return;
    await sb.from('favorites').delete().neq('id', '00000000-0000-0000-0000-000000000000');
    await syncFavs();
}

/* ─── Renderização ─── */
function renderQuote() {
    const q = state.filtered[state.idx];
    const card = document.getElementById('quoteCard');
    card.classList.add('fade-out');
    setTimeout(() => {
        document.getElementById('quoteText').textContent   = q.text;
        document.getElementById('quoteAuthor').textContent = '— ' + q.author;
        document.getElementById('quoteCat').textContent    = CAT_LABELS[q.cat];
        document.getElementById('quoteIndex').textContent  = state.idx + 1;
        document.getElementById('quoteTotal').textContent  = state.filtered.length;
        document.getElementById('favBtn').classList.toggle('fav-active', state.favs.some(f => f.text === q.text));
        card.classList.remove('fade-out');
    }, 280);
}

function renderFavList() {
    document.getElementById('favList').innerHTML = state.favs.length
        ? state.favs.map(f =>
            `<div class="fav-item">
               <p>"${f.text}"</p><span>— ${f.author}</span>
               <button class="rm-fav" data-text="${encodeURIComponent(f.text)}"><i class="fas fa-times"></i></button>
             </div>`).join('')
        : '<p class="empty-msg">Nenhum favorito ainda.</p>';
}

function setTheme(theme) {
    state.theme = theme;
    document.body.setAttribute('data-theme', theme);
    localStorage.setItem('uplift_theme', theme);
    document.querySelectorAll('.theme-dot').forEach(d =>
        d.classList.toggle('active', d.dataset.theme === theme));
}

/* ─── Exportar favoritos ─── */
function exportFavs() {
    if (!state.favs.length) return;
    const lines = state.favs.map(f => `"${f.text}"\n  — ${f.author}`).join('\n\n');
    const full  = `Meus Favoritos — Uplift\n${'─'.repeat(28)}\n\n${lines}\n\nExportado em ${new Date().toLocaleDateString('pt-BR')}`;
    navigator.clipboard.writeText(full).then(() => {
        const btn = document.getElementById('exportFavsBtn');
        const orig = btn.innerHTML;
        btn.textContent = 'Copiado!';
        setTimeout(() => { btn.innerHTML = orig; }, 1800);
    }).catch(() => {
        const blob = new Blob([full], { type: 'text/plain' });
        const a = document.createElement('a');
        a.href = URL.createObjectURL(blob);
        a.download = 'favoritos-uplift.txt';
        a.click();
        URL.revokeObjectURL(a.href);
    });
}

/* ─── Envio de frase sugerida ─── */
async function submitQuote() {
    const text   = document.getElementById('subText').value.trim();
    const author = document.getElementById('subAuthor').value.trim();
    const cat    = document.getElementById('subCat').value;
    if (!text || !author) {
        document.getElementById('subError').textContent = 'A frase e o autor são obrigatórios.';
        return;
    }
    const { error } = await sb.from('quote_submissions').insert({
        text, author, cat, submitted_by: state.userId,
    });
    if (error) {
        document.getElementById('subError').textContent = 'Erro ao enviar. Tente novamente.';
        return;
    }
    document.getElementById('subText').value   = '';
    document.getElementById('subAuthor').value = '';
    document.getElementById('subError').textContent = '';
    document.getElementById('submitModal').classList.remove('open');
    const btn = document.getElementById('btnSubmitQuote');
    const orig = btn.innerHTML;
    btn.textContent = 'Enviado! Obrigado.';
    setTimeout(() => { btn.innerHTML = orig; }, 2500);
}

/* ─── Inicialização ─── */
document.addEventListener('DOMContentLoaded', () => {
    setTheme(state.theme);
    buildFiltered();
    renderQuote();

    initAuth();

    document.querySelectorAll('.cat-btn').forEach(btn =>
        btn.addEventListener('click', () => {
            state.cat = btn.dataset.cat;
            buildFiltered();
            document.querySelectorAll('.cat-btn').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            renderQuote();
        }));

    document.getElementById('nextBtn').addEventListener('click', () => {
        state.idx = (state.idx + 1) % state.filtered.length;
        renderQuote();
    });

    document.getElementById('favBtn').addEventListener('click', () => {
        toggleFav(state.filtered[state.idx]);
    });

    document.getElementById('shareBtn').addEventListener('click', () => {
        const q = state.filtered[state.idx];
        const text = `"${q.text}" — ${q.author}\n\nUplift · frases motivacionais`;
        if (navigator.share) navigator.share({ title: 'Uplift', text });
        else navigator.clipboard.writeText(text).then(() => {
            const btn = document.getElementById('shareBtn');
            const orig = btn.innerHTML;
            btn.textContent = 'Copiado!';
            setTimeout(() => { btn.innerHTML = orig; }, 1800);
        });
    });

    document.getElementById('favToggle').addEventListener('click', () => {
        state.favOpen = !state.favOpen;
        document.getElementById('favPanel').classList.toggle('open', state.favOpen);
        document.getElementById('favToggle').classList.toggle('active', state.favOpen);
        if (state.favOpen) renderFavList();
    });

    document.getElementById('favList').addEventListener('click', e => {
        const btn = e.target.closest('.rm-fav');
        if (!btn) return;
        const text = decodeURIComponent(btn.dataset.text);
        sb.from('favorites').delete().eq('quote_text', text).then(() => syncFavs());
    });

    document.getElementById('exportFavsBtn').addEventListener('click', exportFavs);
    document.getElementById('clearFavs').addEventListener('click', clearAllFavs);

    document.getElementById('btnOpenSubmit').addEventListener('click', () =>
        document.getElementById('submitModal').classList.add('open'));
    document.getElementById('btnCancelSubmit').addEventListener('click', () =>
        document.getElementById('submitModal').classList.remove('open'));
    document.getElementById('btnSubmitQuote').addEventListener('click', submitQuote);
    document.getElementById('submitModal').addEventListener('click', e => {
        if (e.target === document.getElementById('submitModal'))
            document.getElementById('submitModal').classList.remove('open');
    });

    document.querySelectorAll('.theme-dot').forEach(dot =>
        dot.addEventListener('click', () => setTheme(dot.dataset.theme)));

    document.addEventListener('keydown', e => {
        if (e.key === 'ArrowRight' || e.key === ' ') {
            e.preventDefault();
            state.idx = (state.idx + 1) % state.filtered.length;
            renderQuote();
        } else if (e.key === 'ArrowLeft') {
            e.preventDefault();
            state.idx = (state.idx - 1 + state.filtered.length) % state.filtered.length;
            renderQuote();
        }
    });
});

/* ══════════════════════════════════════════════════════════
   PAINEL ADMIN — aprovar/rejeitar frases sugeridas
   Acesso: adicione ?admin à URL
   SQL necessário:
     ALTER TABLE quote_submissions ADD COLUMN IF NOT EXISTS approved BOOLEAN DEFAULT FALSE;
     CREATE POLICY "submissions_select" ON quote_submissions FOR SELECT USING (true);
     CREATE POLICY "submissions_update" ON quote_submissions FOR UPDATE USING (true) WITH CHECK (true);
   ══════════════════════════════════════════════════════════ */
(async function initAdminPanel() {
    if (!new URLSearchParams(window.location.search).has('admin')) return;

    function hesc(s) {
        return String(s ?? '').replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
    }

    const panel = document.createElement('section');
    panel.style.cssText = 'position:fixed;top:0;left:0;width:100%;height:100%;background:var(--bg,#0d0d1a);z-index:9999;overflow-y:auto;padding:2rem;box-sizing:border-box;color:var(--text,#e2e8f0);font-family:Inter,sans-serif';
    panel.innerHTML = `
        <div style="max-width:760px;margin:0 auto">
            <div style="display:flex;align-items:center;justify-content:space-between;margin-bottom:1.5rem">
                <h2 style="margin:0;font-size:1.3rem">⚡ Uplift — Aprovar Frases</h2>
                <button id="adminClose" style="background:none;border:1px solid #444;color:inherit;padding:.4rem .9rem;border-radius:6px;cursor:pointer;font-size:.85rem">✕ Fechar</button>
            </div>
            <div id="adminStats" style="font-size:.85rem;color:#64748b;margin-bottom:1.5rem"></div>
            <div id="adminList" style="display:flex;flex-direction:column;gap:.75rem">
                <p style="color:#64748b">Carregando...</p>
            </div>
        </div>`;
    document.body.appendChild(panel);

    document.getElementById('adminClose').addEventListener('click', () => panel.remove());

    async function loadSubmissions() {
        const { data, error } = await sb.from('quote_submissions')
            .select('*')
            .order('created_at', { ascending: false });

        const list = document.getElementById('adminList');
        const stats = document.getElementById('adminStats');

        if (error) {
            list.innerHTML = `<p style="color:#f87171">Erro: ${hesc(error.message)}<br><small>Execute no Supabase: ALTER TABLE quote_submissions ADD COLUMN IF NOT EXISTS approved BOOLEAN DEFAULT FALSE; CREATE POLICY "submissions_select" ON quote_submissions FOR SELECT USING (true);</small></p>`;
            return;
        }
        if (!data?.length) {
            list.innerHTML = '<p style="color:#64748b">Nenhuma sugestão recebida ainda.</p>';
            return;
        }

        const pending  = data.filter(s => !s.approved);
        const approved = data.filter(s =>  s.approved);
        stats.textContent = `${data.length} total · ${pending.length} pendentes · ${approved.length} aprovadas`;

        list.innerHTML = data.map(s => `
            <div id="row-${s.id}" style="background:#1a1a2e;border:1px solid ${s.approved ? '#22c55e44' : '#ffffff22'};border-radius:10px;padding:1rem 1.25rem">
                <div style="display:flex;justify-content:space-between;align-items:flex-start;gap:1rem;margin-bottom:.5rem;flex-wrap:wrap">
                    <span style="font-size:.75rem;padding:.2rem .6rem;border-radius:4px;background:${s.approved ? '#16a34a33' : '#f5a62333'};color:${s.approved ? '#4ade80' : '#fbbf24'}">${s.approved ? '✓ Aprovada' : '⏳ Pendente'}</span>
                    <small style="color:#64748b;font-size:.75rem">${new Date(s.created_at).toLocaleString('pt-BR')}</small>
                </div>
                <p style="margin:.3rem 0 .2rem;font-size:1rem;line-height:1.5">"${hesc(s.text)}"</p>
                <p style="margin:0 0 .75rem;font-size:.82rem;color:#94a3b8">— ${hesc(s.author)} · <em>${hesc(s.cat)}</em></p>
                <div style="display:flex;gap:.5rem;flex-wrap:wrap">
                    ${!s.approved ? `<button onclick="adminApprove('${s.id}')" style="background:#16a34a;color:#fff;border:none;padding:.35rem .9rem;border-radius:6px;cursor:pointer;font-size:.82rem">✓ Aprovar</button>` : ''}
                    <button onclick="adminDelete('${s.id}')" style="background:#dc262633;color:#f87171;border:1px solid #dc262655;padding:.35rem .9rem;border-radius:6px;cursor:pointer;font-size:.82rem">✕ Remover</button>
                </div>
            </div>`).join('');
    }

    window.adminApprove = async (id) => {
        await sb.from('quote_submissions').update({ approved: true }).eq('id', id);
        await loadSubmissions();
    };
    window.adminDelete = async (id) => {
        if (!confirm('Remover esta sugestão?')) return;
        await sb.from('quote_submissions').delete().eq('id', id);
        await loadSubmissions();
    };

    await loadSubmissions();
})();
