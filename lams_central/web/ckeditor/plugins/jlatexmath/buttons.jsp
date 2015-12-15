<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<div id="formulaTabs">
	<ul>
		<li><a href="#formulaTabGreek"><fmt:message key="ckeditor.math.greek"/></a></li>
		<li><a href="#formulaTabMisc"><fmt:message key="ckeditor.math.misc"/></a></li>
		<li><a href="#formulaTabLarge"><fmt:message key="ckeditor.math.large"/></a></li>
		<li><a href="#formulaTabBinary"><fmt:message key="ckeditor.math.binary"/></a></li>
		<li><a href="#formulaTabRelations"><fmt:message key="ckeditor.math.relations"/></a></li>
		<li><a href="#formulaTabArrows"><fmt:message key="ckeditor.math.arrows"/></a></li>
		<li><a href="#formulaTabCombining"><fmt:message key="ckeditor.math.combining"/></a></li>
	</ul>
	
	<div id="formulaTabGreek">
		<button onclick="javascript:insertFormula('\\alpha ')"   title="alpha">α</button>
		<button onclick="javascript:insertFormula('\\beta ')"    title="beta">β</button>
		<button onclick="javascript:insertFormula('\\gamma ')" 	 title="gamma">γ</button>
		<button onclick="javascript:insertFormula('\\delta ')"   title="delta">δ</button>
		<button onclick="javascript:insertFormula('\\epsilon ')" title="epsilon">ε</button>
		<button onclick="javascript:insertFormula('\\zeta ')"    title="zeta">ζ</button>
		<button onclick="javascript:insertFormula('\\eta ')"     title="eta">η</button>
		<button onclick="javascript:insertFormula('\\theta ')"   title="theta">θ</button>
		<button onclick="javascript:insertFormula('\\iota ')"    title="iota">ι</button>
		<button onclick="javascript:insertFormula('\\kappa ')"   title="kappa">κ</button>
		<button onclick="javascript:insertFormula('\\lambda ')"  title="lambda">λ</button>
		<button onclick="javascript:insertFormula('\\mu ')"      title="mu">μ</button>
		<button onclick="javascript:insertFormula('\\nu ')"      title="nu">ν</button>
		<button onclick="javascript:insertFormula('\\xi ')"      title="xi">ξ</button>
		<button onclick="javascript:insertFormula('\\pi ')"      title="pi">π</button>
		<button onclick="javascript:insertFormula('\\rho ')" 	 title="rho">ρ</button>
		<button onclick="javascript:insertFormula('\\sigma ')"   title="sigma">σ</button>
		<button onclick="javascript:insertFormula('\\tau ')"     title="tau">τ</button>
		<button onclick="javascript:insertFormula('\\upsilon ')" title="upsilon">υ</button>
		<button onclick="javascript:insertFormula('\\phi ')"     title="phi">φ</button>
		<button onclick="javascript:insertFormula('\\chi ')"     title="chi">χ</button>
		<button onclick="javascript:insertFormula('\\psi ')"     title="psi">ψ</button>
		<button onclick="javascript:insertFormula('\\omega ')"   title="omega">ω</button>
		<br />
		<button onclick="javascript:insertFormula('\\Alpha ')"   title="Alpha">Α</button>
		<button onclick="javascript:insertFormula('\\Beta ')"    title="Beta">Β</button>
		<button onclick="javascript:insertFormula('\\Gamma ')" 	 title="Gamma">Γ</button>
		<button onclick="javascript:insertFormula('\\Delta ')"   title="Delta">Δ</button>
		<button onclick="javascript:insertFormula('\\Epsilon ')" title="Epsilon">Ε</button>
		<button onclick="javascript:insertFormula('\\Zeta ')"    title="Zeta">Ζ</button>
		<button onclick="javascript:insertFormula('\\Eta ')"     title="Eta">Η</button>
		<button onclick="javascript:insertFormula('\\Theta ')"   title="Theta">Θ</button>
		<button onclick="javascript:insertFormula('\\Iota ')"    title="Iota">Ι</button>
		<button onclick="javascript:insertFormula('\\Kappa ')"   title="Kappa">Κ</button>
		<button onclick="javascript:insertFormula('\\Lambda ')"  title="Lambda">Λ</button>
		<button onclick="javascript:insertFormula('\\Mu ')"      title="Mu">Μ</button>
		<button onclick="javascript:insertFormula('\\Nu ')"      title="Nu">Ν</button>
		<button onclick="javascript:insertFormula('\\Xi ')"      title="Xi">Ξ</button>
		<button onclick="javascript:insertFormula('\\Pi ')"      title="Pi">Π</button>
		<button onclick="javascript:insertFormula('\\Rho ')" 	 title="Rho">Ρ</button>
		<button onclick="javascript:insertFormula('\\Sigma ')"   title="Sigma">Σ</button>
		<button onclick="javascript:insertFormula('\\Tau ')"     title="Tau">Τ</button>
		<button onclick="javascript:insertFormula('\\Upsilon ')" title="Upsilon">Υ</button>
		<button onclick="javascript:insertFormula('\\Phi ')"     title="Phi">Φ</button>
		<button onclick="javascript:insertFormula('\\Chi ')"     title="Chi">Χ</button>
		<button onclick="javascript:insertFormula('\\Psi ')"     title="Psi">Ψ</button>
		<button onclick="javascript:insertFormula('\\Omega ')"   title="Omega">Ω</button>
	</div>
	<div id="formulaTabMisc">
			<button onclick="javascript:insertFormula('\\aleph ')"   title="aleph" class="formulaButtonShifted">ℵ</button>
			<button onclick="javascript:insertFormula('\\hbar ')"   title="hbar" class="formulaButtonShifted">ℏ</button>
			<button onclick="javascript:insertFormula('\\ell ')"   title="ell">ℓ</button>
			<button onclick="javascript:insertFormula('\\wp ')"   title="wp" class="formulaButtonShifted">℘</button>
			<button onclick="javascript:insertFormula('\\Re ')"   title="Re" class="formulaButtonShifted">ℜ</button>
			<button onclick="javascript:insertFormula('\\Im ')"   title="Im" class="formulaButtonShifted">ℑ</button>
			<button onclick="javascript:insertFormula('\\partial ')"   title="partial">∂</button>
			<button onclick="javascript:insertFormula('\\infty ')"   title="infty" class="formulaButtonShifted">∞</button>
			<button onclick="javascript:insertFormula('\\prime ')"   title="prime">′</button>
			<button onclick="javascript:insertFormula('\\emptyset ')"   title="emptyset">∅</button>
			<button onclick="javascript:insertFormula('\\nabla ')"   title="nabla">∇</button>
			<button onclick="javascript:insertFormula('\\surd ')"   title="surd">√</button>
			<button onclick="javascript:insertFormula('\\top ')"   title="top" class="formulaButtonShifted">⊤</button>
			<button onclick="javascript:insertFormula('\\bot ')"   title="bot" class="formulaButtonShifted">⊥</button>
			<button onclick="javascript:insertFormula('\\| ')"   title="|" class="formulaButtonShifted">∥</button>
			<button onclick="javascript:insertFormula('\\angle ')"   title="angle" class="formulaButtonShifted">∠</button>
			<button onclick="javascript:insertFormula('\\triangle ')"   title="triangle" class="formulaButtonShifted">△</button>
			<button onclick="javascript:insertFormula('\\backslash ')"   title="backslash">\</button>
			<button onclick="javascript:insertFormula('\\forall ')"   title="forall" class="formulaButtonShifted">∀</button>
			<button onclick="javascript:insertFormula('\\exists ')"   title="exists" class="formulaButtonShifted">∃</button>
			<button onclick="javascript:insertFormula('\\neg ')"   title="neg" class="formulaButtonShifted">¬</button>
			<button onclick="javascript:insertFormula('\\flat ')"   title="flat" class="formulaButtonShifted">♭</button>
			<button onclick="javascript:insertFormula('\\natural ')"   title="natural" class="formulaButtonShifted">♮</button>
			<button onclick="javascript:insertFormula('\\sharp ')"   title="sharp">♯</button>
	</div>
	<div id="formulaTabLarge">
		<button onclick="javascript:insertFormula('\\sum ')"   title="sum">∑</button>
		<button onclick="javascript:insertFormula('\\prod ')"   title="prod">∏</button>
		<button onclick="javascript:insertFormula('\\coprod ')"   title="coprod" class="formulaButtonShifted">∐</button>
		<button onclick="javascript:insertFormula('\\int ')"   title="int">∫</button>
		<button onclick="javascript:insertFormula('\\oint ')"   title="oint" class="formulaButtonShifted">∮</button>
		<button onclick="javascript:insertFormula('\\bigcap ')"   title="bigcap" class="formulaButtonShifted">⋂</button>
		<button onclick="javascript:insertFormula('\\bigcup ')"   title="bigcup" class="formulaButtonShifted">⋃</button>
		<button onclick="javascript:insertFormula('\\bigsqcup ')"   title="bigsqcup" class="formulaButtonShifted">⊔</button>
		<button onclick="javascript:insertFormula('\\bigcirc ')"   title="bigcirc" class="formulaButtonShifted">◯</button>
		<button onclick="javascript:insertFormula('\\bigvee ')"   title="bigvee" class="formulaButtonShifted">⋁</button>
		<button onclick="javascript:insertFormula('\\bigwedge ')"   title="bigwedge" class="formulaButtonShifted">⋀</button>
		<button onclick="javascript:insertFormula('\\bigtriangleup ')"   title="bigtriangleup" class="formulaButtonShifted">△</button>
		<button onclick="javascript:insertFormula('\\bigtriangledown ')"   title="bigtriangledown" class="formulaButtonShifted">▽</button>
		<button onclick="javascript:insertFormula('\\bigodot ')"   title="bigodot" class="formulaButtonShifted">⊙</button>
		<button onclick="javascript:insertFormula('\\bigotimes ')"   title="bigotimes" class="formulaButtonShifted">⊗</button>
		<button onclick="javascript:insertFormula('\\bigoplus ')"   title="bigoplus" class="formulaButtonShifted">⊕</button>
		<button onclick="javascript:insertFormula('\\biguplus ')"   title="biguplus" class="formulaButtonShifted">⊎</button>
	</div>
	<div id="formulaTabBinary">
		<button onclick="javascript:insertFormula('\\pm ')"   title="pm">±</button>
		<button onclick="javascript:insertFormula('\\mp ')"   title="mp" class="formulaButtonShifted">∓</button>
		<button onclick="javascript:insertFormula('\\setminus ')"   title="setminus" class="formulaButtonShifted">∖</button>
		<button onclick="javascript:insertFormula('\\cdot ')"   title="cdot" class="formulaButtonShifted">⋅</button>
		<button onclick="javascript:insertFormula('\\times ')"   title="times" class="formulaButtonShifted">×</button>
		<button onclick="javascript:insertFormula('\\ast ')"   title="ast" class="formulaButtonShifted">∗</button>
		<button onclick="javascript:insertFormula('\\star ')"   title="star" class="formulaButtonShifted">⋆</button>
		<button onclick="javascript:insertFormula('\\diamond ')"   title="diamond" class="formulaButtonShifted">⋄</button>
		<button onclick="javascript:insertFormula('\\circ ')"   title="circ" class="formulaButtonShifted">∘</button>
		<button onclick="javascript:insertFormula('\\bullet ')"   title="bullet">∙</button>
		<button onclick="javascript:insertFormula('\\div ')"   title="div">÷</button>
		<button onclick="javascript:insertFormula('\\cap ')"   title="cap">∩</button>
		<button onclick="javascript:insertFormula('\\cup ')"   title="cup" class="formulaButtonShifted">∪</button>
		<button onclick="javascript:insertFormula('\\uplus ')"   title="uplus" class="formulaButtonShifted">⊎</button>
		<button onclick="javascript:insertFormula('\\sqcap ')"   title="sqcap" class="formulaButtonShifted">⊓</button>
		<button onclick="javascript:insertFormula('\\sqcup ')"   title="sqcup" class="formulaButtonShifted">⊔</button>
		<button onclick="javascript:insertFormula('\\triangleleft ')"   title="triangleleft" class="formulaButtonShifted">◁</button>
		<button onclick="javascript:insertFormula('\\triangleright ')"   title="triangleright" class="formulaButtonShifted">▷</button>
		<button onclick="javascript:insertFormula('\\wr ')"   title="wr" class="formulaButtonShifted">≀</button>
		<button onclick="javascript:insertFormula('\\vee ')"   title="vee" class="formulaButtonShifted">∨</button>
		<button onclick="javascript:insertFormula('\\wedge ')"   title="wedge" class="formulaButtonShifted">∧</button>
		<button onclick="javascript:insertFormula('\\oplus ')"   title="oplus" class="formulaButtonShifted">⊕</button>
		<button onclick="javascript:insertFormula('\\ominus ')"   title="ominus" class="formulaButtonShifted">⊖</button>
		<button onclick="javascript:insertFormula('\\otimes ')"   title="otimes" class="formulaButtonShifted">⊗</button>
		<button onclick="javascript:insertFormula('\\oslash ')"   title="oslash" class="formulaButtonShifted">⊘</button>
		<button onclick="javascript:insertFormula('\\odot ')"   title="odot" class="formulaButtonShifted">⊙</button>
		<button onclick="javascript:insertFormula('\\dagger ')"   title="dagger">†</button>
		<button onclick="javascript:insertFormula('\\ddagger ')"   title="ddagger">‡</button>
		<button onclick="javascript:insertFormula('\\amalg ')"   title="amalg" class="formulaButtonShifted">∐</button>
	</div>
	<div id="formulaTabRelations">
		<button onclick="javascript:insertFormula('\\leq ')" title="leq">≤</button>
		<button onclick="javascript:insertFormula('\\geq ')" title="geq">≥</button>
		<button onclick="javascript:insertFormula('\\prec ')" title="prec" class="formulaButtonShifted">≺</button>
		<button onclick="javascript:insertFormula('\\succ ')" title="succ" class="formulaButtonShifted">≻</button>
		<button onclick="javascript:insertFormula('\\preceq ')" title="preceq" class="formulaButtonShifted">≼</button>
		<button onclick="javascript:insertFormula('\\succeq ')" title="succeq" class="formulaButtonShifted">≽</button>
		<button onclick="javascript:insertFormula('\\ll ')" title="ll" class="formulaButtonShifted">≪</button>
		<button onclick="javascript:insertFormula('\\gg ')" title="gg" class="formulaButtonShifted">≫</button>
		<button onclick="javascript:insertFormula('\\equiv ')" title="equiv" class="formulaButtonShifted">≡</button>
		<button onclick="javascript:insertFormula('\\sim ')" title="sim" class="formulaButtonShifted">∼</button>
		<button onclick="javascript:insertFormula('\\simeq ')" title="simeq" class="formulaButtonShifted">≃</button>
		<button onclick="javascript:insertFormula('\\asymp ')" title="asymp" class="formulaButtonShifted">≍</button>
		<button onclick="javascript:insertFormula('\\approx ')" title="approx" class="formulaButtonShifted">≈</button>
		<button onclick="javascript:insertFormula('\\cong ')" title="cong" class="formulaButtonShifted">≅</button>
		<button onclick="javascript:insertFormula('\\doteq ')" title="doteq" class="formulaButtonShifted">≐</button>
		<button onclick="javascript:insertFormula('\\subset ')" title="subset" class="formulaButtonShifted">⊂</button>
		<button onclick="javascript:insertFormula('\\supset ')" title="supset" class="formulaButtonShifted">⊃</button>
		<button onclick="javascript:insertFormula('\\subseteq ')" title="subseteq" class="formulaButtonShifted">⊆</button>
		<button onclick="javascript:insertFormula('\\supseteq ')" title="supseteq" class="formulaButtonShifted">⊇</button>
		<button onclick="javascript:insertFormula('\\sqsubseteq ')" title="sqsubseteq" class="formulaButtonShifted">⊑</button>
		<button onclick="javascript:insertFormula('\\sqsubseteq ')" title="sqsubseteq" class="formulaButtonShifted">⊒</button>
		<button onclick="javascript:insertFormula('\\in ')" title="in" class="formulaButtonShifted">∈</button>
		<button onclick="javascript:insertFormula('\\ni ')" title="ni" class="formulaButtonShifted">∋</button>
		<button onclick="javascript:insertFormula('\\vdash ')" title="vdash" class="formulaButtonShifted">⊢</button>
		<button onclick="javascript:insertFormula('\\dashv ')" title="dashv" class="formulaButtonShifted">⊣</button>
		<button onclick="javascript:insertFormula('\\perp ')" title="perp" class="formulaButtonShifted">⊥</button>
		<button onclick="javascript:insertFormula('\\models ')" title="models" class="formulaButtonShifted">⊨</button>
		<button onclick="javascript:insertFormula('\\mid ')" title="mid" class="formulaButtonShifted">∣</button>
		<button onclick="javascript:insertFormula('\\parallel ')" title="parallel" class="formulaButtonShifted">∥</button>
		<button onclick="javascript:insertFormula('\\bowtie ')" title="bowtie" class="formulaButtonShifted">⋈</button>
		<button onclick="javascript:insertFormula('\\propto ')" title="propto" class="formulaButtonShifted">∝</button>
		<br />
		<button onclick="javascript:insertFormula('\\nless ')" title="nless" class="formulaButtonShifted">≮</button>
		<button onclick="javascript:insertFormula('\\ngtr ')" title="ngtr" class="formulaButtonShifted">≯</button>
		<button onclick="javascript:insertFormula('\\not\\leq ')" title="notleq" class="formulaButtonShifted">≰</button>
		<button onclick="javascript:insertFormula('\\not\\geq ')" title="notgeq" class="formulaButtonShifted">≱</button>
		<button onclick="javascript:insertFormula('\\not\\prec ')" title="notprec" class="formulaButtonShifted">⊀</button>
		<button onclick="javascript:insertFormula('\\not\\succ ')" title="notsucc" class="formulaButtonShifted">⊁</button>
		<button onclick="javascript:insertFormula('\\not\\preceq ')" title="notpreceq" class="formulaButtonShifted">⋠</button>
		<button onclick="javascript:insertFormula('\\not\\succeq ')" title="not\\succeq" class="formulaButtonShifted">⋡</button>
		<button onclick="javascript:insertFormula('\\neq ')" title="neq" class="formulaButtonShifted">≠</button>
		<button onclick="javascript:insertFormula('\\not\\equiv ')" title="notequiv" class="formulaButtonShifted">≢</button>
		<button onclick="javascript:insertFormula('\\not\\sim ')" title="notsim" class="formulaButtonShifted">≁</button>
		<button onclick="javascript:insertFormula('\\not\\simeq ')" title="notsimeq" class="formulaButtonShifted">≄</button>
		<button onclick="javascript:insertFormula('\\not\\approx ')" title="notapprox" class="formulaButtonShifted">≉</button>
		<button onclick="javascript:insertFormula('\\not\\cong ')" title="notcong" class="formulaButtonShifted">≇</button>
		<button onclick="javascript:insertFormula('\\not\\asymp ')" title="notasymp" class="formulaButtonShifted">≭</button>
		<button onclick="javascript:insertFormula('\\not\\subset ')" title="notsubset" class="formulaButtonShifted">⊄</button>
		<button onclick="javascript:insertFormula('\\not\\supset ')" title="notsupset" class="formulaButtonShifted">⊅</button>
		<button onclick="javascript:insertFormula('\\not\\subseteq ')" title="notsubseteq" class="formulaButtonShifted">⊈</button>
		<button onclick="javascript:insertFormula('\\not\\supseteq ')" title="notsupseteq" class="formulaButtonShifted">⊉</button>
		<button onclick="javascript:insertFormula('\\not\\sqsubseteq ')" title="notsqsubseteq" class="formulaButtonShifted">⋢</button>
		<button onclick="javascript:insertFormula('\\not\\sqsupseteq ')" title="notsqsupseteq" class="formulaButtonShifted">⋣</button>
	</div>
	<div id="formulaTabArrows">
		<button onclick="javascript:insertFormula('\\leftarrow ')" title="leftarrow" class="formulaButtonShifted">←</button>
		<button onclick="javascript:insertFormula('\\rightarrow ')" title="rightarrow" class="formulaButtonShifted">→</button>
		<button onclick="javascript:insertFormula('\\Leftarrow ')" title="Leftarrow" class="formulaButtonShifted">⇐</button>
		<button onclick="javascript:insertFormula('\\Rightarrow ')" title="Rightarrow" class="formulaButtonShifted">⇒</button>
		<button onclick="javascript:insertFormula('\\hookleftarrow ')" title="hookleftarrow" class="formulaButtonShifted">↩</button>
		<button onclick="javascript:insertFormula('\\hookrightarrow ')" title="hookrightarrow" class="formulaButtonShifted">↪</button>
		<button onclick="javascript:insertFormula('\\leftharpoonup ')" title="leftharpoonup" class="formulaButtonShifted">↼</button>
		<button onclick="javascript:insertFormula('\\rightharpoonup ')" title="rightharpoonup" class="formulaButtonShifted">⇀</button>
		<button onclick="javascript:insertFormula('\\leftharpoondown ')" title="leftharpoondown" class="formulaButtonShifted">↽</button>
		<button onclick="javascript:insertFormula('\\rightharpoondown ')" title="rightharpoondown" class="formulaButtonShifted">⇁</button>
		<button onclick="javascript:insertFormula('\\leftrightarrow ')" title="leftrightarrow" class="formulaButtonShifted">↔</button>
		<button onclick="javascript:insertFormula('\\Leftrightarrow ')" title="Leftrightarrow" class="formulaButtonShifted">⇔</button>
		<button onclick="javascript:insertFormula('\\rightleftharpoons ')" title="rightleftharpoons" class="formulaButtonShifted">⇌</button>
		<button onclick="javascript:insertFormula('\\uparrow ')" title="uparrow" class="formulaButtonShifted">↑</button>
		<button onclick="javascript:insertFormula('\\downarrow ')" title="downarrow" class="formulaButtonShifted">↓</button>
		<button onclick="javascript:insertFormula('\\Uparrow ')" title="Uparrow" class="formulaButtonShifted">⇑</button>
		<button onclick="javascript:insertFormula('\\Downarrow ')" title="Downarrow" class="formulaButtonShifted">⇓</button>
		<button onclick="javascript:insertFormula('\\updownarrow ')" title="updownarrow" class="formulaButtonShifted">↕</button>
		<button onclick="javascript:insertFormula('\\Updownarrow ')" title="Updownarrow" class="formulaButtonShifted">⇕</button>
		<button onclick="javascript:insertFormula('\\nearrow ')" title="nearrow" class="formulaButtonShifted">↗</button>
		<button onclick="javascript:insertFormula('\\searrow ')" title="searrow" class="formulaButtonShifted">↘</button>
		<button onclick="javascript:insertFormula('\\swarrow ')" title="swarrow" class="formulaButtonShifted">↙</button>
		<button onclick="javascript:insertFormula('\\nwarrow ')" title="nwarrow" class="formulaButtonShifted">↖</button>
	</div>
	<div id="formulaTabCombining">
		<button onclick="javascript:insertFormula('\\' ')">◌́ </button>
		<button onclick="javascript:insertFormula('\\` ')">◌̀ </button>
		<button onclick="javascript:insertFormula('\\^ ')">◌̂ </button>
		<button onclick="javascript:insertFormula('\\\" ')">◌̈ </button>
		<button onclick="javascript:insertFormula('\\~ ')">◌̃ </button>
		<button onclick="javascript:insertFormula('\\= ')">◌̄ </button>
		<button onclick="javascript:insertFormula('\\. ')">◌̇ </button>
		<button onclick="javascript:insertFormula('\\u ')">◌̆ </button>
		<button onclick="javascript:insertFormula('\\v ')">◌̌ </button>
		<button onclick="javascript:insertFormula('\\H ')">◌̋ </button>
		<button onclick="javascript:insertFormula('\\t ')">◌͡ </button>
		<button onclick="javascript:insertFormula('\\c ')">◌̧ </button>
		<button onclick="javascript:insertFormula('\\d ')">◌̣ </button>
		<button onclick="javascript:insertFormula('\\b ')">◌̱ </button>
		<button onclick="javascript:insertFormula('\\not ')">◌̸ </button>
	</div>
</div>