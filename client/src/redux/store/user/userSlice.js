import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { logout } from "../../../actions/post/logout";
import { cartActions } from "../shopping-cart/cartSlice";
import { cartActionsLiked } from "../shopping-cart/cartsLikedSlice";

const accessToken =
	localStorage.getItem("accessToken") !== null
		? JSON.parse(localStorage.getItem("accessToken"))
		: null;

const setUserAccessTokenFunc = (accessToken) => {
	localStorage.setItem("accessToken", JSON.stringify(accessToken));
};

const initialState = {
	accessToken: accessToken,
	isAuthenticated: accessToken ? true : false,
	firstName: null,
	lastName: null,
	email: null,
};

export const axiosLogout = createAsyncThunk(
	"user/axiosLogout",
	async ({ accessToken }, { rejectWithValue, dispatch }) => {
		try {
			const response = await logout(accessToken);

			console.log(response);
			if (response.status === 200) {
				dispatch(clearUser());
				dispatch(cartActions.clearCart());
				dispatch(cartActionsLiked.clearCartsLiked());
			}
		} catch (error) {
			return rejectWithValue({
				message: error.message,
				code: error.code,
				response_data: error.response?.data,
				response_status: error.response?.status,
			});
		}
	}
);

const userSlice = createSlice({
	name: "user",
	initialState,
	reducers: {
		setInfoUser: (state, action) => {
			state.firstName = action.payload.firstName;
			state.lastName = action.payload.lastName;
			state.email = action.payload.email;
			console.log("setInfoUser", state.accessToken, state.isAuthenticated);
		},
		setUser: (state, action) => {
			// console.log(action.payload);
			state.accessToken = action.payload.accessToken;
			state.isAuthenticated = true;
			setUserAccessTokenFunc(state.accessToken);
			// console.log("setUser", state.accessToken, state.isAuthenticated);
		},
		clearUser: (state) => {
			console.log("P");
			state.accessToken = null;
			state.firstName = null;
			state.lastName = null;
			state.email = null;
			state.isAuthenticated = false;
			setUserAccessTokenFunc(state.accessToken);
			console.log("clearUser", state.accessToken, state.isAuthenticated);
		},
	},
	extraReducers: (builder) => {
		builder.addCase(axiosLogout.fulfilled, (state, action) => {
			console.log(state, action);
		});
		builder.addCase(axiosLogout.rejected, (state, action) => {
			console.log(action.payload); // Handle the error
		});
	},
});

export const { setUser, clearUser, setInfoUser } = userSlice.actions;
export default userSlice.reducer;
