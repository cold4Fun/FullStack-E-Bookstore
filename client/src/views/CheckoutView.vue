<script setup lang="ts">
import { reactive, Ref } from "vue";
import useVuelidate from "@vuelidate/core";
import { asDollarsAndCents } from "@/main";
import {
  helpers,
  maxLength,
  email,
  minLength,
  required,
} from "@vuelidate/validators";
import { useCartStore } from "@/stores/CartStore";
import { useCategoryStore } from "@/stores/CategoryStore";
const categoryStore = useCategoryStore();
const cartStore = useCartStore();
const cart = cartStore.cart;
import { isCreditCard, isMobilePhone } from "@/utils";
import CheckoutFieldError from "@/components/CheckoutFieldError.vue";
import router from "@/router";

const months: string[] = [
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December",
];
const years: string[] = [
  "2022",
  "2023",
  "2024",
  "2025",
  "2026",
  "2027",
  "2028",
  "2029",
  "2030",
  "2031",
  "2032",
  "2033",
  "2034",
  "2035",
];

const form = reactive({
  name: "",
  address: "",
  phone: "",
  email: "",
  ccNumber: "",
  ccExpiryMonth: new Date().getMonth() + 1,
  ccExpiryYear: new Date().getFullYear(),
  checkoutStatus: "",
});

const rules = {
  name: {
    required: helpers.withMessage("Please provide a name.", required),
    minLength: helpers.withMessage(
      "Name must have at least 4 letters.",
      minLength(4)
    ),
    maxLength: helpers.withMessage(
      "Name can have at most 45 letters.",
      maxLength(45)
    ),
  },
  address: {
    required: helpers.withMessage("Please provide an address.", required),
    minLength: helpers.withMessage(
      "Your address must have at least 4 letters.",
      minLength(4)
    ),
    maxLength: helpers.withMessage(
      "Your address  can have at most 45 letters.",
      maxLength(45)
    ),
  },
  phone: {
    required: helpers.withMessage("Please provide a phone number.", required),
    phone: helpers.withMessage(
      "Please provide a valid phone number.",
      (value: string) => !helpers.req(value) || isMobilePhone(value)
    ),
  },
  email: {
    required: helpers.withMessage("Please provide an email address.", required),
    email: helpers.withMessage("Please provide a valid email address.", email),
  },
  ccNumber: {
    required: helpers.withMessage("Please provide a credit card.", required),
    ccNumber: helpers.withMessage(
      "Please provide a valid credit card.",
      (value: string) => !helpers.req(value) || isCreditCard(value)
    ),
  },
  ccExpiryMonth: {},
  ccExpiryYear: {},
};
const $v = useVuelidate(rules, form);

async function submitOrder() {
  console.log("Submit order");
  const isFormCorrect = await $v.value.$validate();
  if (!isFormCorrect) {
    form.checkoutStatus = "ERROR";
  } else {
    form.checkoutStatus = "PENDING";
    await cartStore
      .placeOrder({
        name: form.name,
        address: form.address,
        phone: form.phone,
        email: form.email,
        ccNumber: form.ccNumber,
        ccExpiryMonth: form.ccExpiryMonth,
        ccExpiryYear: form.ccExpiryYear,
      })
      .then(() => {
        form.checkoutStatus = "OK";
        // cartStore.clearCart();
        router.push({ name: "confirmation-view" });
      })
      .catch((reason) => {
        form.checkoutStatus = "SERVER_ERROR";
        console.log("Error placing order", reason);
      });
  }
}

/* NOTE: For example yearFrom(0) == <current_year> */
function yearFrom(index: number) {
  return new Date().getFullYear() + index;
}
</script>

<style scoped>
/* TODO: Adapt these styles to your page */
.checkout-page {
  justify-content: right;
  background: rgb(253, 253, 253);
  color: #000000;
}
.checkout-page-body {
  justify-content: center;
  display: flex;
  padding: 1em;
  flex-direction: column;
  align-items: center;
}

form {
  display: flex;
  align-items: flex-end;
  flex-direction: column;
}

form > div {
  padding: 1em;
  display: flex;
  justify-content: flex-end;
  margin-bottom: 0.5em;
}

form > div > input,
form > div > select {
  background-color: #ffaf00;
  margin-left: 0.5em;
}

form > .error {
  color: red;
  text-align: right;
}

.checkoutStatusBox {
  margin: 1em;
  padding: 1em;
  text-align: center;
}
* {
  box-sizing: border-box;
}

.selector-container {
  position: relative;
  width: fit-content;
  font-size: 1rem;
}

.selector-dropdown {
  appearance: none;
  height: 1.5rem;
  padding: 0 2.5em 0 1em;
  border: solid #999 1px;
}

.selector-icon {
  font-size: 0.9rem;
  position: relative;
  top: 0;
  right: 0;
  padding-top: 0.2em;
  background-color: rgba(197, 145, 0, 0.88);
  color: white;
  text-align: center;
  display: block;
  height: 1.5rem;
  width: 1.5rem;
  pointer-events: none;
}
</style>

<template>
  <div class="checkout-page">
    <!-- TODO: Add an HTML section to display when checking out with an empty cart -->
    <section id="checkout-empty" v-if="cart.empty">
      <p>Your cart is empty.</p>
      <p>Please add an item to your cart to checkout.</p>
      <router-link
        :to="{
          name: 'category-view',
          params: {
            name: categoryStore.categoryName,
          },
        }"
      >
        <button class="continue-shop-buttons">
          <font-awesome-icon icon="fa-solid fa-circle-arrow-left" />
          Continue Shopping
        </button>
      </router-link>
    </section>

    <section class="checkout-page-body" v-if="!cart.empty">
      <form @submit.prevent="submitOrder">
        <div>
          <label for="name">Name</label>
          <input
            type="text"
            size="20"
            id="name"
            name="name"
            v-model.lazy="$v.name.$model"
          />
        </div>
        <CheckoutFieldError field-name="name" :validator="$v" />

        <div>
          <label for="address">Address</label>
          <input
            type="text"
            size="20"
            id="address"
            name="address"
            v-model.lazy="$v.address.$model"
          />
        </div>

        <CheckoutFieldError field-name="address" :validator="$v" />

        <!-- TODO: Add address input and validation messages -->

        <div>
          <label for="phone">Phone</label>
          <input
            type="text"
            size="20"
            id="phone"
            name="phone"
            v-model.lazy="$v.phone.$model"
          />
        </div>
        <CheckoutFieldError field-name="phone" :validator="$v" />
        <!-- TODO: Add phone validation message(s) -->

        <div>
          <label for="email">Email</label>
          <input
            type="text"
            size="20"
            id="email"
            name="email"
            v-model.lazy="$v.email.$model"
          />
        </div>
        <CheckoutFieldError field-name="email" :validator="$v" />

        <div>
          <label for="ccNumber">Credit card</label>
          <input
            type="text"
            size="20"
            id="ccNumber"
            name="ccNumber"
            v-model.lazy="$v.ccNumber.$model"
          />
        </div>
        <CheckoutFieldError field-name="ccNumber" :validator="$v" />

        <!-- TODO: Add credit card validation message(s) -->

        <div class="selector-container">
          <label>Exp Month</label>
          <select class="selector-dropdown" v-model="$v.ccExpiryMonth.$model">
            <option
              v-for="(month, index) in months"
              :key="index"
              :value="index + 1"
            >
              {{ month }} ({{ index + 1 }})
            </option>
          </select>
          <div class="selector-icon"><i class="fas fa-sort"></i></div>
        </div>

        <div class="selector-container">
          <label>Exp Year</label>
          <select class="selector-dropdown" v-model="$v.ccExpiryYear.$model">
            <option
              v-for="(year, index) in years"
              :key="index"
              :value="yearFrom(index)"
            >
              {{ year }}
            </option>
          </select>
          <div class="selector-icon"><i class="fas fa-sort"></i></div>
        </div>
        <div class="heading-subtotal">
          <div class="row-line"></div>
          <div class="row-line"></div>
          <div class="row-line"></div>
          <div class="row-line" v-if="cartStore.count > 1">
            <p>
              Today You need to pay
              {{ asDollarsAndCents(cartStore.cart.total) }}
            </p>
            <p>for {{ cartStore.count }} Books.</p>
          </div>
          <div v-else>
            <p>
              Today You need to pay
              {{ asDollarsAndCents(cartStore.cart.total) }}
            </p>
            <p>
              for
              {{ cartStore.count }} Book.
            </p>
          </div>
        </div>

        <input
          type="submit"
          name="submit"
          class="button"
          :disabled="form.checkoutStatus === 'PENDING'"
          @click.prevent="submitOrder"
          value="Complete Purchase"
        />
        <!-- TODO (style): The submit button should not take up the entire width of the form. -->
        <!-- TODO (style): The submit button should be styled consistent with your own site. -->
      </form>
      <!-- TODO (style): Fix error message placement so they nearer to the correct fields. -->
      <!-- TODO (style): HINT: Use another <div> and label, input, and error, and use flexbox to style. -->

      <!-- TODO: Display the cart total, subtotal and surcharge. -->

      <section v-show="form.checkoutStatus !== ''" class="checkoutStatusBox">
        <div v-if="form.checkoutStatus === 'ERROR'">
          Error: Please fix the problems above and try again.
        </div>

        <div v-else-if="form.checkoutStatus === 'PENDING'">Processing...</div>

        <div v-else-if="form.checkoutStatus === 'OK'">Order placed...</div>

        <div v-else>An unexpected error occurred, please try again.</div>
      </section>
    </section>
  </div>
</template>
