/**
 * Provides the model classes for the API responses within the MavenToys application. This package
 * includes generic and specific response wrappers and data transfer objects (DTOs) that facilitate
 * the communication between the server and clients. The primary goal of these models is to ensure
 * a consistent and flexible structure for all API responses, improving the client's ability to handle
 * data and errors.
 * <p>
 * The {@link com.oreilly.maventoys.model.CustomApiResponse} class is a cornerstone of this package,
 * offering a unified response structure that includes both a message for context and a generic payload
 * for data. This design allows for a wide range of data types to be returned in a standardized way,
 * simplifying client-side data processing. Additional classes in this package, such as mappers and DTOs,
 * support the conversion and handling of entity data in a form suitable for external use.
 * <p>
 * Utilizing Lombok annotations for boilerplate code reduction, the model classes emphasize simplicity
 * and readability, ensuring that API response structures are both robust and easy to maintain.
 */
package com.oreilly.maventoys.model;
