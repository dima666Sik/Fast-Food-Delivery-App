import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { logout } from "../../../actions/post/logout";
import { cartActions } from "../shopping-cart/cartSlice";
import { cartActionsLiked } from "../shopping-cart/cartsLikedSlice";
import { decodeToken } from "react-jwt";

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
	role: null,
};

export const axiosLogout = createAsyncThunk(
	"user/axiosLogout",
	async ({ accessToken }, { rejectWithValue, dispatch }) => {
		try {
			const response = await logout(accessToken);

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
		setUser: (state, action) => {
			// console.log(action.payload);
			state.accessToken = action.payload.accessToken;
			state.isAuthenticated = true;
			const decodedToken = decodeToken(action.payload.accessToken);
			if (decodedToken) {
				state.firstName = decodedToken.first_name;
				state.lastName = decodedToken.last_name;
				state.email = decodedToken.sub;
				state.role = decodedToken.role;
			}
			setUserAccessTokenFunc(state.accessToken);
			console.log("setUser: ", { ...state });
		},

		clearUser: (state) => {
			console.log("P");
			state.accessToken = null;
			state.firstName = null;
			state.lastName = null;
			state.email = null;
			state.role = null;
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

export const { setUser, clearUser } = userSlice.actions;
export default userSlice.reducer;
