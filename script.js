const SUPABASE_URL      = 'https://bvquyfzllqnbfxncsacn.supabase.co';
const SUPABASE_ANON_KEY = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImJ2cXV5ZnpsbHFuYmZ4bmNzYWNuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzgxODU1MzQsImV4cCI6MjA5Mzc2MTUzNH0.xa_rs4bVLoTv58P7U8rDOaPjo1Dqt60q8cR-IWFpbug';
const sb = supabase.createClient(SUPABASE_URL, SUPABASE_ANON_KEY);

const QUOTES = [
    { text:"The only way to do great work is to love what you do.", author:"Steve Jobs", cat:"motivacao" },
    { text:"You don't have to be great to start, but you have to start to be great.", author:"Zig Ziglar", cat:"motivacao" },
    { text:"Perseverance is the path to success.", author:"Charles Chaplin", cat:"motivacao" },
    { text:"Don't wait. The time will never be just right.", author:"Napoleon Hill", cat:"motivacao" },
    { text:"Start where you are. Use what you have. Do what you can.", author:"Arthur Ashe", cat:"motivacao" },
    { text:"Action is the foundational key to all success.", author:"Pablo Picasso", cat:"motivacao" },
    { text:"Dreams don't work unless you do.", author:"John C. Maxwell", cat:"motivacao" },
    { text:"The secret of getting ahead is getting started.", author:"Mark Twain", cat:"motivacao" },
    { text:"You are braver than you believe, stronger than you seem.", author:"A. A. Milne", cat:"motivacao" },
    { text:"The difference between the impossible and the possible lies in determination.", author:"Tommy Lasorda", cat:"motivacao" },
    { text:"Don't give up. The beginning is always the hardest.", author:"Unknown", cat:"motivacao" },
    { text:"Every day is a new opportunity to change your life.", author:"Unknown", cat:"motivacao" },
    { text:"Success is the sum of small efforts repeated day after day.", author:"Robert Collier", cat:"sucesso" },
    { text:"Success usually comes to those who are too busy to be looking for it.", author:"Henry David Thoreau", cat:"sucesso" },
    { text:"Don't measure success by what you've achieved, but by the obstacles you've overcome.", author:"Booker T. Washington", cat:"sucesso" },
    { text:"Success is going from failure to failure without loss of enthusiasm.", author:"Winston Churchill", cat:"sucesso" },
    { text:"The difference between ordinary and extraordinary is that little extra.", author:"Jimmy Johnson", cat:"sucesso" },
    { text:"Winners never quit and quitters never win.", author:"Vince Lombardi", cat:"sucesso" },
    { text:"To succeed, you first have to believe that you can.", author:"Nikos Kazantzakis", cat:"sucesso" },
    { text:"Failure is the opportunity to begin again, this time more intelligently.", author:"Henry Ford", cat:"sucesso" },
    { text:"Work while they sleep, learn while they party.", author:"Unknown", cat:"sucesso" },
    { text:"Love does not consist of gazing at each other, but in looking together in the same direction.", author:"Antoine de Saint-Exupéry", cat:"amor" },
    { text:"To love is to find your own happiness in the happiness of another.", author:"Gottfried Leibniz", cat:"amor" },
    { text:"Love is the only thing that grows when shared.", author:"Antoine de Saint-Exupéry", cat:"amor" },
    { text:"The best proof of love is trust.", author:"Joyce Brothers", cat:"amor" },
    { text:"Where there is love there is life.", author:"Mahatma Gandhi", cat:"amor" },
    { text:"Love heals people — both the ones who give it and the ones who receive it.", author:"Karl Menninger", cat:"amor" },
    { text:"Life is what happens while you are busy making other plans.", author:"John Lennon", cat:"vida" },
    { text:"In the middle of every difficulty lies opportunity.", author:"Albert Einstein", cat:"vida" },
    { text:"Live as if you were to die tomorrow. Learn as if you were to live forever.", author:"Mahatma Gandhi", cat:"vida" },
    { text:"Life is too short to be small.", author:"Benjamin Disraeli", cat:"vida" },
    { text:"Don't cry because it's over. Smile because it happened.", author:"Gabriel García Márquez", cat:"vida" },
    { text:"What doesn't kill you makes you stronger.", author:"Friedrich Nietzsche", cat:"vida" },
    { text:"Be the change you wish to see in the world.", author:"Mahatma Gandhi", cat:"vida" },
    { text:"Enjoy every moment. Time is the most precious asset.", author:"Unknown", cat:"vida" },
    { text:"Know thyself.", author:"Socrates", cat:"sabedoria" },
    { text:"Our greatest glory is not in never falling, but in rising every time we fall.", author:"Confucius", cat:"sabedoria" },
    { text:"Wisdom begins when we know what we do not know.", author:"Socrates", cat:"sabedoria" },
    { text:"Education is the most powerful weapon you can use to change the world.", author:"Nelson Mandela", cat:"sabedoria" },
    { text:"An investment in knowledge always pays the best interest.", author:"Benjamin Franklin", cat:"sabedoria" },
    { text:"Doubt is the beginning of wisdom.", author:"Aristotle", cat:"sabedoria" },
];

const CAT_LABELS = { all:"All", motivacao:"Motivation", sucesso:"Success", amor:"Love", vida:"Life", sabedoria:"Wisdom" };

const state = {
    cat: "all", idx: 0, filtered: [],
    favs: [],           // local cache synced with the database
    theme: localStorage.getItem("uplift_theme") || "purple",
    favOpen: false,
    userId: null,
};

/* ─── Deterministic algorithm — same quote of the day for everyone ─── */
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

/* ─── Anonymous auth — no login, but persistent identity ─── */
async function initAuth() {
    const { data: { session } } = await sb.auth.getSession();
    if (session) {
        state.userId = session.user.id;
    } else {
        const { data } = await sb.auth.signInAnonymously();
        state.userId = data.user?.id;
    }
    if (state.userId) await syncFavs();
}

/* ─── Favorites (database) ─── */
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
    if (!confirm('Remove all favorites?')) return;
    await sb.from('favorites').delete().neq('id', '00000000-0000-0000-0000-000000000000');
    await syncFavs();
}

/* ─── Render ─── */
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
        : '<p class="empty-msg">No favorites yet.</p>';
}

function setTheme(theme) {
    state.theme = theme;
    document.body.setAttribute('data-theme', theme);
    localStorage.setItem('uplift_theme', theme);
    document.querySelectorAll('.theme-dot').forEach(d =>
        d.classList.toggle('active', d.dataset.theme === theme));
}

/* ─── Export favorites ─── */
function exportFavs() {
    if (!state.favs.length) return;
    const lines = state.favs.map(f => `"${f.text}"\n  — ${f.author}`).join('\n\n');
    const full  = `My Favorites — Uplift\n${'─'.repeat(28)}\n\n${lines}\n\nExported on ${new Date().toLocaleDateString('en-US')}`;
    navigator.clipboard.writeText(full).then(() => {
        const btn = document.getElementById('exportFavsBtn');
        const orig = btn.innerHTML;
        btn.textContent = 'Copied!';
        setTimeout(() => { btn.innerHTML = orig; }, 1800);
    }).catch(() => {
        const blob = new Blob([full], { type: 'text/plain' });
        const a = document.createElement('a');
        a.href = URL.createObjectURL(blob);
        a.download = 'favorites-uplift.txt';
        a.click();
        URL.revokeObjectURL(a.href);
    });
}

/* ─── Quote submission ─── */
async function submitQuote() {
    const text   = document.getElementById('subText').value.trim();
    const author = document.getElementById('subAuthor').value.trim();
    const cat    = document.getElementById('subCat').value;
    if (!text || !author) {
        document.getElementById('subError').textContent = 'Quote and author are required.';
        return;
    }
    const { error } = await sb.from('quote_submissions').insert({
        text, author, cat, submitted_by: state.userId,
    });
    if (error) {
        document.getElementById('subError').textContent = 'Error sending. Please try again.';
        return;
    }
    document.getElementById('subText').value   = '';
    document.getElementById('subAuthor').value = '';
    document.getElementById('subError').textContent = '';
    document.getElementById('submitModal').classList.remove('open');
    const btn = document.getElementById('btnSubmitQuote');
    const orig = btn.innerHTML;
    btn.textContent = 'Sent! Thank you.';
    setTimeout(() => { btn.innerHTML = orig; }, 2500);
}

/* ─── Init ─── */
document.addEventListener('DOMContentLoaded', () => {
    setTheme(state.theme);
    buildFiltered();
    renderQuote();

    initAuth(); // start anonymous session and load favorites from database

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
        const text = `"${q.text}" — ${q.author}\n\nUplift · motivational quotes`;
        if (navigator.share) navigator.share({ title: 'Uplift', text });
        else navigator.clipboard.writeText(text).then(() => {
            const btn = document.getElementById('shareBtn');
            const orig = btn.innerHTML;
            btn.textContent = 'Copied!';
            setTimeout(() => { btn.innerHTML = orig; }, 1800);
        });
    });

    document.getElementById('favToggle').addEventListener('click', () => {
        state.favOpen = !state.favOpen;
        document.getElementById('favPanel').classList.toggle('open', state.favOpen);
        document.getElementById('favToggle').classList.toggle('active', state.favOpen);
        if (state.favOpen) renderFavList();
    });

    /* Event delegation — remover favorito */
    document.getElementById('favList').addEventListener('click', e => {
        const btn = e.target.closest('.rm-fav');
        if (!btn) return;
        const text = decodeURIComponent(btn.dataset.text);
        sb.from('favorites').delete().eq('quote_text', text).then(() => syncFavs());
    });

    document.getElementById('exportFavsBtn').addEventListener('click', exportFavs);
    document.getElementById('clearFavs').addEventListener('click', clearAllFavs);

    /* Submission modal */
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
