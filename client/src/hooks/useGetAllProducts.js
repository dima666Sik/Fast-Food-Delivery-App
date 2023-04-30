import axios from "axios";
import { useEffect, useState } from "react";

export const useGetAllProducts = () => {
	const [products, setProducts] = useState([]);
	const [isLoading, setIsLoading] = useState(false);

	useEffect(() => {
		setIsLoading(true);
		axios
			.get(
				`${process.env.REACT_APP_SERVER_API_URL}api/v1/foods/get-all-products`
			)
			.then((response) => {
				// setTimeout(() => {
				const updatedProducts = response.data.map((product) => {
					return {
						...product,
						image01: `${process.env.REACT_APP_SERVER_API_URL}public/images/products/${product.image01}`,
						image02: `${process.env.REACT_APP_SERVER_API_URL}public/images/products/${product.image02}`,
						image03: `${process.env.REACT_APP_SERVER_API_URL}public/images/products/${product.image03}`,
					};
				});
				setProducts(updatedProducts);
				// }, 4000);
			})
			.catch((error) => {
				console.log(error);
			})
			.finally(() => setIsLoading(false));
	}, []);

	return { products, setProducts, isLoading, setIsLoading };
};
