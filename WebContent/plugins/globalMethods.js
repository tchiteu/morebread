const MyPlugin = {}

MyPlugin.install = function (Vue) {
  Vue.mixin({
    methods: {
      hello() {
        alert("BOm dai dacalr")
      }
    }
  })

}

export default MyPlugin
