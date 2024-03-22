import axios from "axios";

export const register = async (userData) => {
	try {
		const response = await axios.post(
			`${process.env.REACT_APP_SERVER_API_URL}api/v1/auth/registration`,
			userData
		);
		return response;
	} catch (error) {
		throw error;
	}
};
