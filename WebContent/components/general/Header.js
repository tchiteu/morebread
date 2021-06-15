const template = /*html*/ `
  <header class="m-header">
    <div class="logo">
      <img src="../../morebread/static/imgs/logo.svg" />
      <h2>Morebread</h2>
    </div>

    <div class="header-items">
      <RouterLink to="/produtos">Produtos</RouterLink>
      <RouterLink to="/vendas">Vendas</RouterLink>
      <RouterLink v-if="validaGestor()" to="/usuarios">Usuarios</RouterLink>
      <RouterLink v-if="validaGestor()" to="/relatorio">Relatório</RouterLink>
    </div>

    <v-btn class="btn-logout" @click="logout"> Sair </v-btn>
  </header>
`;

export default {
  template,
  methods: {
    validaGestor() {
      return $auth.isManager()
    }
  }
}