package com.example.data

import com.example.data.model.BubbleCategory
import com.example.data.model.BubbleKeyword

data class SeedCategory(
    val category: BubbleCategory,
    val keywords: List<String>
)

object InitialBubbleData {
    val seedData: List<SeedCategory> = listOf(
        SeedCategory(
            category = BubbleCategory(
                id = 1,
                name = "Mecânica Pesada & Tratores",
                description = "Manutenção diesel, sistemas hidráulicos agrícolas e maquinário pesado",
                iconEmoji = "🚜",
                colorHex = "#F59E0B"
            ),
            keywords = listOf(
                "regulagem de valvulas motor diesel trator",
                "bomba injetora bosch reparo bicos",
                "sistema hidraulico 3 pontos trator valtra",
                "substituicao embreagem dupla massey ferguson",
                "esteira de colheitadeira lubrificacao rolamentos",
                "filtro decantador combustivel diesel s10",
                "torque cabecote motor perkins 4 cilindros",
                "esquema eletrico alternador case ih",
                "diferencial traseiro trator john deere folga",
                "reforma tomada de potencia tdp agricola"
            )
        ),
        SeedCategory(
            category = BubbleCategory(
                id = 2,
                name = "Astrofísica & Mecânica Quântica",
                description = "Cosmologia, partículas fundamentais e física teórica avançada",
                iconEmoji = "🌌",
                colorHex = "#8B5CF6"
            ),
            keywords = listOf(
                "emaranhamento quantico paradoxo epr bell",
                "espectro de emissao hidrogenio serie balmer",
                "fusao termonuclear estelar ciclo cno",
                "horizonte de eventos buraco negro de kerr",
                "radiacao hawking entropia bekenstein",
                "telescopio james webb espectroscopia infravermelho",
                "bóson de higgs mecanismo quebra de simetria",
                "materia escura curvas de rotacao galactica",
                "relatividade geral equacoes de campo einstein",
                "interferometria ligo deteccao ondas gravitacionais"
            )
        ),
        SeedCategory(
            category = BubbleCategory(
                id = 3,
                name = "Jardinagem, Bonsai & Botânica",
                description = "Cultivo vegetal avançado, substratos técnicos e arte do bonsai",
                iconEmoji = "🌱",
                colorHex = "#10B981"
            ),
            keywords = listOf(
                "poda apical pinheiro negro japones bonsai",
                "preparo substrato organico akadama kiryu",
                "adubacao bokashi fermentado proporcoes npk",
                "propagacao por estaquia estacas lenhosas azaleia",
                "enxertia em fenda cheia macieiras porta enxerto",
                "hidroponia caseira solucao nutritiva condutividade",
                "aramacao galhos coniferas tecnica de torcao",
                "controle fungico calda bordalesa concentracao",
                "rega e drenagem suculentas deserto echeveria",
                "compostagem termofilica relacao carbono nitrogenio"
            )
        ),
        SeedCategory(
            category = BubbleCategory(
                id = 4,
                name = "Culinária Étnica & Fermentação",
                description = "Fermentação milenar, micróbios benéficos e gastronomia regional tradicional",
                iconEmoji = "🍲",
                colorHex = "#EC4899"
            ),
            keywords = listOf(
                "kimchi tradicional coreano pasta gochugaru repolho",
                "fermento natural levain sourdough hidratacao 75",
                "miso artesanal koji fermentacao arroz soja",
                "scoby kombucha primeira fermentacao acido acetico",
                "chucrute alemao fermentacao liofilizada lactobacillus",
                "defumacao a frio salmao cura sal marinho",
                "molho garum peixe receita romana antiga fermentada",
                "tempeh indonesio cultura rhizopus oligosporus",
                "queijo artesanal coalho canastra afinacao fungos",
                "pao pita autentico forno alta temperatura bolha de ar"
            )
        ),
        SeedCategory(
            category = BubbleCategory(
                id = 5,
                name = "Tricô, Crochê & Tecelagem",
                description = "Trabalhos manuais têxteis, fiação tradicional e pontos clássicos",
                iconEmoji = "🧶",
                colorHex = "#06B6D4"
            ),
            keywords = listOf(
                "ponto arroz duplo trico agulhas circulares",
                "tecelagem manual em tear de pente lico urdidura",
                "fiacao de la merino em fuso de mao torcao",
                "receita amigurumi ponto baixo espiral aumentos",
                "tingimento natural tecidos casca de cebola mordente",
                "ponto cruz grafico medieval tapeçaria jacquard",
                "bloqueio pecas trico com vapor e alfinetes",
                "bainha aberta desfiada bordado richelieu",
                "cordao i cord duas agulhas acabamento",
                "la virgem cardacao de fibras vegetais linho"
            )
        ),
        SeedCategory(
            category = BubbleCategory(
                id = 6,
                name = "Numismática & Moedas Históricas",
                description = "Estudo arqueológico de moedas, ligas metálicas e impérios antigos",
                iconEmoji = "🪙",
                colorHex = "#EAB308"
            ),
            keywords = listOf(
                "denario de prata imperador augusto cunhagem romana",
                "moeda 960 reis carimbo de minas patacão",
                "patina natural conservacao moedas de bronze antigas",
                "dobrao de ouro d joao v 1724 casa da moeda bahia",
                "graduacao numismatica estado de conservacao flor de cunho",
                "tetradracma atenas coruja prata helenistica",
                "moeda colonial holandesa brasil recife obsidional",
                "pesagem de precisao moedas antigas teor de prata",
                "catalogo bentes moedas brasileiras variantes raras",
                "identificacao bordo serrilhado cunho descentralizado"
            )
        ),
        SeedCategory(
            category = BubbleCategory(
                id = 7,
                name = "Apicultura & Abelhas Nativas",
                description = "Meliponicultura, criação de abelhas sem ferrão e biologia da colmeia",
                iconEmoji = "🐝",
                colorHex = "#F97316"
            ),
            keywords = listOf(
                "criacao de abelha jataí caixa racional modelo inpa",
                "manejo meliponario mandacaia divisao de enxame",
                "alimentacao artificial xarope invertido meliponas",
                "propolis verde producao e extrato alcoolico",
                "fumigador apicultura serragem de eucalipto acendimento",
                "cera de abelha alveolada laminacao para melgueira",
                "rainha africana postura geleia real enxameacao",
                "armadilha garrafa pet captura abelhas nativas mirim",
                "colheita de mel sucção a vacuo bomba de melipona",
                "polinizacao cruzada especies nativas mata atlantica"
            )
        ),
        SeedCategory(
            category = BubbleCategory(
                id = 8,
                name = "Engenharia Ferroviária & Locomotivas",
                description = "Infraestrutura de trilhos, sistemas de sinalização e tração pesada",
                iconEmoji = "🚂",
                colorHex = "#3B82F6"
            ),
            keywords = listOf(
                "dormentes de concreto protendido fixacao pandrol",
                "freio a ar comprimido westinghouse valvula tripla",
                "truque ferroviario bitola mista vagao hopper",
                "locomotiva diesel eletrica ge ac44 motor de tracao",
                "aparelho de mudanca de via amv agulha e jacare",
                "solda aluminotermica trilho continuo trc",
                "sinalizacao ctc controle de trafego centralizado",
                "catenaria alimentacao aerea pantografo 25kv",
                "perfil de roda ferroviaria friso desgaste conico",
                "lastro de brita ferrovia desguarnecedora de via"
            )
        ),
        SeedCategory(
            category = BubbleCategory(
                id = 9,
                name = "Marcenaria Tradicional & Madeira",
                description = "Juntas tradicionais japonesas, afiação fina e ferramentas manuais",
                iconEmoji = "🪚",
                colorHex = "#D97706"
            ),
            keywords = listOf(
                "encaixe rabo de andorinha manual serra dozuki",
                "afiacao formoes oire nomi pedra d'agua grano 8000",
                "plaina japonesa kanna ajuste de cunha e contracunha",
                "junta de madeira kanawa tsugi carpintaria tradicional",
                "acabamento oleo de tungue puro cura polimerizacao",
                "bancada de marceneiro moxon morsa de fuso duplo",
                "raspadeira de gabinete corte rebarba fio afiado",
                "madeira peroba rosa de demolicao plainamento",
                "graminho de corte regulagem de precisao madeira macica",
                "cera de carnauba e abelha polimento acabamento acetinado"
            )
        ),
        SeedCategory(
            category = BubbleCategory(
                id = 10,
                name = "Ornitologia & Aves Silvestres",
                description = "Bioacústica, rotas migratórias e ecologia de aves continentais",
                iconEmoji = "🦅",
                colorHex = "#14B8A6"
            ),
            keywords = listOf(
                "espectrograma bioacustica canto do uirapuru verdadeiro",
                "identificacao rapina gaviao real ninho dossel",
                "rotas migratorias falcao peregrino anilhamento",
                "binoculo prisma porro 8x42 observacao florestal",
                "dimorfismo sexual tangara saira sete cores",
                "nidificacao joao de barro estrutura concha argila",
                "guia de campo aves do pantanal e cerrado plumagem",
                "microfone parabolico gravador som ambiente passaros",
                "comportamento lek corte nupcial danca tangara",
                "censo de aves florestais metodologia ponto de escuta"
            )
        )
    )
}
