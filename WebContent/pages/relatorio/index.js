const template = /*html*/ `
  <div>
    <m-header></m-header>

    <v-row class="pa-4 justify-center">
      <v-col cols="4"> 
        <v-menu
          ref="menu"
          v-model="menu"
          :close-on-content-click="false"
          :return-value.sync="datas"
          transition="scale-transition"
          offset-y
          min-width="auto"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              v-model="datasTexto"
              label="Período"
              placeholder="Selecione a data de início"
              prepend-icon="mdi-calendar"
              readonly
              v-bind="attrs"
              v-on="on"
            ></v-text-field>
          </template>
          
          <v-date-picker v-model="datas" range locale="pt-br" no-title @blur="$refs.menu.save(datas)" />
        </v-menu>
      </v-col>

      <v-col cols="7" class="d-flex align-center justify-end">
        <v-btn 
          @click="geraRelatorio"
          color="success"
          :disabled="!datas[1]"
        >
          Gerar Relatório
        </v-btn>
      </v-col>
    </v-row>

    <v-row class="pa-4 justify-center">
      <!-- TABELA -->
      <v-col cols="11">
        <v-card class="pa-8 text-left custom-border" tile>
          <h3>Relatório vendas</h3>
          <small>Detalhes de cada produto vendido dentro de um período</small>
          
          <v-data-table 
            class="elevation-2 mt-3"
            :headers="headers"
            :items="detalhesVendas"
            :loading="carregando"
            disable-sort
            hide-default-footer
            loading-text="Buscando..."
            no-data-text="Nada encontrado..."
          >
            <template v-slot:item.dataRealizada="{ item }">
              {{ formataData(item.dataRealizada) }}
            </template>

            <template v-slot:item.valor="{ item }">
              R$ {{ item.valor.toFixed(2) }}
            </template>
          </v-data-table>

          <v-btn
            @click="exportaPDF"
            color="blue"
            class="mt-4"
            :disabled="!detalhesVendas[0]"
          >
            Exportar PDF
          </v-btn>
        </v-card>
      </v-col>
    </v-row>
  </div>
`

export default {
	template,

	data: () => ({
    datas: [],
    datasTexto: "",
    menu: false,
		detalhesVendas: [],
    carregando: false,
    valorTotalVendas: "R$ 0",
    quantidadeTotalVendas: 0,
    
    headers: [
      { text: "Venda ID", value: "vendaId" },
			{ text: "Data", value: "dataRealizada" },
			{ text: "Usuário", value: "usuarioNome" },
			{ text: "Produto", value: "produtoNome" },
			{ text: "Quantidade", value: "quantidade" },
			{ text: "Valor", value: "valor" }
    ],

		// Rules
		campoObrigatorio: v => !!v || 'Campo obrigatório'
	}),
	methods: {
    async geraRelatorio() {
      this.carregando = true;

      await axios.get(`/relatorios/vendas?dt_inicio=${this.datas[0]}&dt_fim=${this.datas[1]}`)
      .then(retorno => {
        this.detalhesVendas = retorno.data.detalhesVendas;
      })
      .catch(() => {
        this.$toasted.error("Erro ao buscar relatório");
      })
      .finally(() => this.carregando = false);
    },

    salvaDatas() {
      this.datas = this.datas;
    },

    calculaTotais() {
      let cont = 0;
      let valorTotal = 0;
      let detalheAntigo = {};

      this.detalhesVendas.forEach(detalhe => {
        valorTotal += detalhe.valor;

        if(detalheAntigo?.vendaId != detalhe.vendaId) {
          cont++;
        }

        detalheAntigo = detalhe;
      })

      this.valorTotalVendas = `R$ ${valorTotal}`;
      this.quantidadeTotalVendas = cont;
    },

    exportaPDF() {
      const nomeArquivo = "relatorio_vendas";
      
      // Monta o cabeçalho (colunas)
      const colunas = this.headers.map(column => {
        return column.text;
      })
      
      // Ordena os valores de cada linha
      const linhas = this.detalhesVendas.map(detalhe => {
        let linha = [];
        
        for (const chave in detalhe) {
          switch (chave) {
            case "vendaId": {
              linha[0] = detalhe[chave];
              break;
            }
            case "dataRealizada": {
              linha[1] = this.formataData(detalhe[chave]);
              break;
            }
            case "usuarioNome": {
              linha[2] = detalhe[chave];
              break;
            }
            case "produtoNome": {
              linha[3] = detalhe[chave];
              break;
            }
            case "quantidade": {
              linha[4] = detalhe[chave];
              break;
            }
            case "valor": {
              linha[5] = `R$ ${detalhe[chave].toFixed(2)}`;
              break;
            }
            default: {
              linha.push(detalhe[chave]);
            }
          }
        }
        
        return linha
      })

      this.calculaTotais()

      // Adiciona os totais na tabela
      linhas.push([])
      linhas.push([`Total Vendas: ${this.quantidadeTotalVendas}`, `Valor Total: R$ ${this.valorTotalVendas}`])

      // Salva a data de hoje
      const data = new Date();
      const dia = data.getDate();
      const mes = data.getMonth();
      const ano = data.getFullYear();

      const dataHoje = `${dia}/${mes}/${ano}`;

      const doc = new jspdf.jsPDF({
        orientation: 'portrait',
        unit: 'cm',
        format: 'A4'
      })

      doc.autoTable(colunas, linhas, {
        startY: doc.pageCount > 1? doc.autoTableEndPosY() + 20 : 4,
        didDrawPage: data => {
          if(data.pageCount === 1) {
            doc.setFontSize(10);
            doc.setFontSize(14)
            doc.text(`Relatório vendas - Emitido ${dataHoje}`, data.settings.margin.left + 5, 2.2)
            doc.setFontSize(10);
            doc.line(data.settings.margin.left, 3, 19.6, 3);
            doc.setFontSize(10);
          }
        }
      })

      doc.save(`${nomeArquivo}.pdf`)
    }
  },
  watch: {
    datas(novasDatas) {
      if (novasDatas.length == 1) {
        this.datasTexto = this.formataData(novasDatas[0]);
      }

      if (novasDatas.length == 2) {
        let data_inicio = this.formataData(novasDatas[0]);
        let data_fim = this.formataData(novasDatas[1]);

        this.datasTexto = `${data_inicio} - ${data_fim}`;
        this.$refs.menu.save(novasDatas);
      }
    }
  }
}