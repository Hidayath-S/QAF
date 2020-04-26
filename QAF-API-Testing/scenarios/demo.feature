Given user wants to create new company details with name as Seventy One
When a request to create new company details
Then postCompanyDetails returns a response which
      has status code 200 
      includes headers
        "Content-Type" with value "application/json"
      has a matching body with company details

