import axios from "axios";

export const setStatus = async (idProduct, status, accessToken) => {
	try {
		await axios.put(
			`${process.env.REACT_APP_SERVER_API_URL}api/v2/product/private/product-like/set-status-like-product`,
			{ product_id: idProduct, status },
			{
				headers: {
					Authorization: "Bearer " + accessToken,
				},
			}
		);
	} catch (error) {
		console.log(error.response.data.message);
		throw error;
	}
};
