package com.example.data

import com.example.data.model.BubbleCategory

data class SeedCategory(
    val category: BubbleCategory,
    val keywords: List<String>
)

/**
 * =====================  CATÁLOGO DE TEMAS  =====================
 *
 * Aqui ficam as categorias e os termos que o app sorteia.
 *
 * COMO EDITAR (sem saber programar):
 *  - Mudar um termo: troque o texto entre aspas.
 *  - Tirar um termo: apague a linha inteira dele.
 *  - Adicionar um termo: copie uma linha de termo, cole embaixo e mude o texto.
 *    Atenção: todo termo termina com vírgula, MENOS o último de cada categoria.
 *  - Nova categoria: copie um bloco inteiro "cat( ... )", cole embaixo e troque tudo.
 *    Cada bloco termina com ")," e o último bloco da lista termina só com ")".
 *
 * Cada categoria tem:
 *   nome, emoji, cor (código #RRGGBB), hashtag do Instagram (UMA palavra, sem #),
 *   descrição e, depois, os termos de busca.
 *
 * DICAS:
 *  - Termos curtos e naturais (como você digitaria na busca) dão mais resultados.
 *  - Use acentos normalmente.
 *  - A hashtag precisa existir no Instagram. Teste digitando #palavra lá antes.
 *
 * IMPORTANTE: o app só lê este arquivo quando o banco de dados está vazio.
 * Depois de mudar aqui, aumente o número "version" em AppDatabase.kt (2 vira 3)
 * para o app recriar o catálogo. Isso apaga categorias que você criou dentro do app.
 * Para mudanças pequenas, é mais fácil editar direto na aba "Bolhas" do app.
 * =================================================================
 */
object InitialBubbleData {

    private fun cat(
        nome: String,
        emoji: String,
        cor: String,
        hashtag: String,
        descricao: String,
        vararg termos: String
    ) = SeedCategory(
        category = BubbleCategory(
            name = nome,
            description = descricao,
            iconEmoji = emoji,
            colorHex = cor,
            instagramTag = hashtag
        ),
        keywords = termos.toList()
    )

    val seedData: List<SeedCategory> = listOf(

        cat(
            "Apicultura & Abelhas Nativas", "🐝", "#F59E0B", "apicultura",
            "Criação de abelhas com e sem ferrão e a vida dentro da colmeia",
            "como começar na apicultura",
            "criação de abelha jataí",
            "meliponicultura para iniciantes",
            "como funciona uma colmeia",
            "colheita de mel passo a passo",
            "abelhas sem ferrão do Brasil",
            "caixa racional para abelhas nativas",
            "importância da polinização"
        ),

        cat(
            "Bonsai & Jardinagem", "🌱", "#10B981", "bonsai",
            "Cultivo de plantas, bonsai, orquídeas e hortas pequenas",
            "bonsai para iniciantes",
            "como podar bonsai",
            "poda de formação em bonsai",
            "substrato para bonsai",
            "jardim vertical como fazer",
            "cultivo de suculentas",
            "como cuidar de orquídeas",
            "horta em apartamento"
        ),

        cat(
            "Astronomia & Física", "🔭", "#8B5CF6", "astronomia",
            "Universo, partículas e as ideias grandes da física",
            "como funcionam os buracos negros",
            "mecânica quântica explicada",
            "o que é matéria escura",
            "telescópio para iniciantes",
            "fases da lua explicadas",
            "como medir distâncias no universo",
            "teoria da relatividade simples",
            "exoplanetas descobertas recentes"
        ),

        cat(
            "Tratores & Mecânica Pesada", "🚜", "#D97706", "tratores",
            "Motores diesel, hidráulica e máquinas agrícolas",
            "como funciona um motor diesel",
            "manutenção básica de trator",
            "como funciona sistema hidráulico",
            "como funciona uma colheitadeira",
            "restauração de trator antigo",
            "como funciona injeção diesel",
            "mecânica de motor para iniciantes",
            "máquinas agrícolas antigas"
        ),

        cat(
            "Fermentação & Culinária Regional", "🍞", "#EF4444", "fermentacao",
            "Pães, queijos, conservas fermentadas e comidas regionais",
            "pão de fermentação natural",
            "como fazer kimchi",
            "kombucha caseira",
            "queijo artesanal em casa",
            "culinária regional brasileira",
            "como fazer missô",
            "iogurte caseiro fermentado",
            "pratos típicos do nordeste"
        ),

        cat(
            "Tricô, Crochê & Tecelagem", "🧶", "#EC4899", "trico",
            "Trabalhos manuais com fios, lã e tear",
            "tricô para iniciantes",
            "crochê amigurumi passo a passo",
            "pontos de tricô",
            "como fiar lã",
            "tecelagem em tear",
            "macramê para iniciantes",
            "bordado livre para iniciantes",
            "história da tapeçaria"
        ),

        cat(
            "Numismática & História Antiga", "🪙", "#EAB308", "numismatica",
            "Moedas, cédulas e o que elas contam sobre impérios antigos",
            "moedas antigas do Brasil",
            "como identificar moeda rara",
            "história do dinheiro",
            "moedas romanas",
            "como limpar moeda antiga",
            "cédulas raras do Brasil",
            "achados arqueológicos de moedas",
            "impérios antigos história"
        ),

        cat(
            "Ferrovias & Trens", "🚂", "#64748B", "ferrovias",
            "Locomotivas, trilhos, sinalização e ferreomodelismo",
            "história das ferrovias no Brasil",
            "como funciona locomotiva a vapor",
            "ferreomodelismo para iniciantes",
            "trens mais rápidos do mundo",
            "como funciona a sinalização ferroviária",
            "estrada de ferro Madeira-Mamoré",
            "locomotiva diesel elétrica como funciona",
            "passeio de maria fumaça"
        ),

        cat(
            "Marcenaria & Madeira", "🪚", "#92400E", "marcenaria",
            "Ferramentas manuais, encaixes e acabamento em madeira",
            "marcenaria para iniciantes",
            "encaixes japoneses em madeira",
            "como afiar formão",
            "tipos de madeira e usos",
            "como fazer um banco de madeira",
            "como usar plaina manual",
            "acabamento verniz em madeira",
            "ferramentas manuais de marcenaria"
        ),

        cat(
            "Aves & Observação da Natureza", "🦜", "#06B6D4", "aves",
            "Aves silvestres, cantos, migração e observação em campo",
            "observação de aves para iniciantes",
            "aves do Pantanal",
            "canto de pássaros brasileiros",
            "migração de aves",
            "ninho de joão-de-barro",
            "binóculo para observar aves",
            "aves do cerrado",
            "fotografia de aves"
        ),

        cat(
            "Música & Instrumentos", "🎻", "#A855F7", "lutheria",
            "Como os instrumentos funcionam e a música de diferentes tradições",
            "como funciona um violino",
            "história da música erudita",
            "construção de violão lutheria",
            "música modal explicada",
            "instrumentos tradicionais brasileiros",
            "como afinar cavaquinho",
            "como funciona uma orquestra",
            "música folclórica nordestina"
        ),

        cat(
            "Geologia & Minerais", "🪨", "#78716C", "geologia",
            "Rochas, cristais, vulcões, fósseis e cavernas",
            "como se formam os cristais",
            "tipos de rochas",
            "como funcionam os vulcões",
            "como se formam os fósseis",
            "mineralogia para iniciantes",
            "terremotos explicados",
            "cavernas e espeleologia",
            "pedras preciosas do Brasil"
        ),

        cat(
            "Idiomas & Linguística", "🗣️", "#14B8A6", "linguistica",
            "Origem das línguas, escritas antigas e curiosidades das palavras",
            "origem das línguas",
            "como aprender latim",
            "etimologia de palavras",
            "línguas indígenas do Brasil",
            "alfabetos do mundo",
            "como funciona o esperanto",
            "escrita cuneiforme",
            "dialetos do português"
        ),

        cat(
            "Oceanos & Vida Marinha", "🌊", "#0EA5E9", "vidamarinha",
            "Mar profundo, marés, corais e animais marinhos",
            "vida marinha em águas profundas",
            "como funcionam as marés",
            "recifes de corais",
            "comportamento das baleias",
            "tubarões curiosidades",
            "mergulho livre para iniciantes",
            "correntes marítimas explicadas",
            "importância do plâncton"
        ),

        cat(
            "Xadrez & Jogos de Estratégia", "♟️", "#6366F1", "xadrez",
            "Xadrez, go, damas e raciocínio estratégico",
            "xadrez para iniciantes",
            "aberturas de xadrez",
            "finais de xadrez básicos",
            "história do xadrez",
            "partidas famosas de xadrez",
            "jogo de go para iniciantes",
            "estratégias de damas",
            "táticas de xadrez"
        ),

        cat(
            "Fotografia Analógica & Cinema Antigo", "📷", "#F43F5E", "fotografiaanalogica",
            "Filmes, câmeras antigas e a história da imagem",
            "fotografia analógica para iniciantes",
            "como revelar filme em casa",
            "câmeras antigas",
            "história do cinema mudo",
            "lentes manuais vintage",
            "cianotipia como fazer",
            "história da fotografia",
            "câmera pinhole caseira"
        )
    )
}
