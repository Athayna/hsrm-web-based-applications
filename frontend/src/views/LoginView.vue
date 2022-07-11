<template>
    <h1>Login</h1>

    <b-form @submit="onSubmit" @reset="onReset">
      <b-form-group id="input-group-1" label="Username:" label-for="input-1">
        <b-form-input
          id="input-1"
          v-model="username"
          type="text"
          placeholder="Enter username"
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-2" label="Password:" label-for="input-2">
        <b-form-input
          id="input-2"
          v-model="password"
          type="password"
          placeholder="Enter password"
          required
        ></b-form-input>
      </b-form-group>

      <b-button type="submit" variant="primary">Submit</b-button>
      <b-button type="reset" variant="danger">Reset</b-button>
    </b-form>


</template>

<script setup lang="ts">
    import router from '@/router';
    import { useLogin } from '@/services/useLogin';
    import { onMounted, ref } from 'vue';

    const username = ref("");
    const password = ref("");

    const onSubmit = async () => {
        await useLogin().login(username.value, password.value);
        if (useLogin().logindata.loggedin) {
            router.push("/");
        } else {
            alert("Login failed");
            onReset();
        }
    }

    const onReset = () => {
        username.value = "";
        password.value = "";
    }

    onMounted(() => {
        useLogin().logout();
    })
</script>