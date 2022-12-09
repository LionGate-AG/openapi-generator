/**
 * Example
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import { Cat } from '../models/Cat';
import { Dog } from '../models/Dog';
import { HttpFile } from '../http/http';

export class PetsPatchRequest {
    'hunts'?: boolean;
    'age'?: number;
    'bark'?: boolean;
    'breed'?: PetsPatchRequestBreedEnum;

    static readonly discriminator: string | undefined = "petType";

    static readonly attributeTypeMap: Array<{name: string, baseName: string, type: string, format: string}> = [
        {
            "name": "hunts",
            "baseName": "hunts",
            "type": "boolean",
            "format": ""
        },
        {
            "name": "age",
            "baseName": "age",
            "type": "number",
            "format": ""
        },
        {
            "name": "bark",
            "baseName": "bark",
            "type": "boolean",
            "format": ""
        },
        {
            "name": "breed",
            "baseName": "breed",
            "type": "PetsPatchRequestBreedEnum",
            "format": ""
        }    ];

    static getAttributeTypeMap() {
        return PetsPatchRequest.attributeTypeMap;
    }

    public constructor() {
        this.petType = "PetsPatchRequest";
    }
}


export type PetsPatchRequestBreedEnum = "Dingo" | "Husky" | "Retriever" | "Shepherd" ;

