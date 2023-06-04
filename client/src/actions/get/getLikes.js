import axios from "axios";

export const getListStatusForUser = async (accessToken) => {
	try {
		const response = await axios.get(
			`${process.env.REACT_APP_SERVER_API_URL}api/v1/private/product-like/get-status-products`,
			{
				headers: {
					Authorization: "Bearer " + accessToken,
				},
			}
		);
		console.log(response.data);
		return response.data;
	} catch (error) {
		throw error;
	}
};
