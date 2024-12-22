import axios from "axios";

export const setCart = async (idProduct, likes, accessToken) => {
	try {
		const response = await axios.post(
			`${process.env.REACT_APP_SERVER_API_URL}api/v2/product/private/product-like/set-like-product`,
			{ product_id: idProduct, likes },
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
