ALTER TABLE prices
ADD CONSTRAINT fk_pharmacy_price
FOREIGN KEY (pharmacy_id) REFERENCES pharmacies(id);

ALTER TABLE prices
ADD CONSTRAINT fk_medicine_price
FOREIGN KEY (medicine_id) REFERENCES medicines(id);
