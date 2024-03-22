import axios from "axios";

export const login = async (email, password) => {
	try {
		const response = await axios.post(
			`${process.env.REACT_APP_SERVER_API_URL}api/v1/auth/login`,
			{ email, password }
		);
		return response;
	} catch (error) {
		throw error;
	}
};
