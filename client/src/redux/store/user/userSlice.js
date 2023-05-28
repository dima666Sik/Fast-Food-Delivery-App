import { createSlice } from "@reduxjs/toolkit";

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
			console.log(action.payload);
			state.accessToken = action.payload.accessToken;
			state.isAuthenticated = true;
			setUserAccessTokenFunc(state.accessToken);
			console.log("setUser", state.accessToken, state.isAuthenticated);
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
});

export const { setUser, clearUser, setInfoUser } = userSlice.actions;
export default userSlice.reducer;
