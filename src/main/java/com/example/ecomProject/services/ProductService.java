package com.example.ecomProject.services;

import com.example.ecomProject.exceptions.ItemDoesntExistException;
import com.example.ecomProject.models.Product;
import com.example.ecomProject.models.dtos.CreateProductRequest;
import com.example.ecomProject.models.dtos.ProductResponse;
import com.example.ecomProject.models.dtos.UpdateProductRequest;
import com.example.ecomProject.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService
{
    @Autowired
    private ProductRepo rp;

    private ProductResponse prodToProdResp(Product prod)
    {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductID(prod.getId());
        productResponse.setProductName(prod.getName());
        productResponse.setBrand(prod.getBrand());
        productResponse.setPrice(prod.getPrice());
        productResponse.setInStock(prod.getQuantity());
        return productResponse;
    }

    private Product productRequestToProduct(CreateProductRequest productRequest)
    {
        Product prod = new Product();
        prod.setName(productRequest.getName());
        prod.setDescription(productRequest.getDescription());
        prod.setQuantity(productRequest.getQuantity());
        prod.setBrand(productRequest.getBrand());
        prod.setPrice(productRequest.getPrice());
        prod.setCategory(productRequest.getCategory());

        return prod;
    }

    public ProductResponse save(CreateProductRequest prod)
    {
        Product p = rp.save(productRequestToProduct(prod));

        return prodToProdResp(p);
    }

    public ProductResponse update(int productId, UpdateProductRequest updatedProduct)
    {
        Optional<Product> prod = rp.findById(productId);

        if (prod.isEmpty())
            throw new ItemDoesntExistException();

        Product existingProduct = prod.get();

        if (updatedProduct.getName() != null)
            existingProduct.setName(updatedProduct.getName());
        if (updatedProduct.getDescription() != null)
            existingProduct.setDescription(updatedProduct.getDescription());
        if (updatedProduct.getBrand() != null)
            existingProduct.setBrand(updatedProduct.getBrand());
        if (updatedProduct.getPrice() != null)
            existingProduct.setPrice(updatedProduct.getPrice());
        if (updatedProduct.getCategory() != null)
            existingProduct.setCategory(updatedProduct.getCategory());
        if (updatedProduct.getQuantity() != null)
            existingProduct.setQuantity(updatedProduct.getQuantity());

        rp.save(existingProduct);

        return prodToProdResp(existingProduct);
    }

    public List<ProductResponse> findAll()
    {
        List<Product> products = rp.findAll();
        List<ProductResponse> prodResponses = new ArrayList<>();

        for (Product p : products)
        {
            prodResponses.add(prodToProdResp(p));
        }

        return prodResponses;
    }

    public void delete(int productId)
    {
        Optional<Product> prod = rp.findById(productId);

        if (prod.isEmpty())
            throw new ItemDoesntExistException();

        rp.deleteById(productId);
    }

    public ProductResponse findById(int productId)
    {
        Optional<Product> prod = rp.findById(productId);

        if (prod.isEmpty())
            throw new ItemDoesntExistException();

        return prodToProdResp(prod.get());
    }

    public List<ProductResponse> findByCategory(String category)
    {
        List<Product> products = rp.findByCategory(category);
        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product p : products)
        {
            productResponses.add(prodToProdResp(p));
        }

        return productResponses;
    }

    public List<ProductResponse> findRelated(String category, Set<Integer> idsPresent)
    {
        List<Product> products = rp.findByCategory(category); // rp is the product repository
        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product p : products)
        {
            if (idsPresent.contains(p.getId()) == false)
                productResponses.add(prodToProdResp(p));
        }
        return productResponses;
    }
}
