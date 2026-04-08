import re

def is_valid_email(email):
    if not email or email == "invalid_email":
        return False
    # More lenient email validation - just check for @ and .
    return "@" in str(email) and "." in str(email)

def is_valid_phone(phone):
    if not phone:
        return False
    # Very lenient phone validation - just check for at least 7 characters
    phone_str = str(phone).strip()
    return len(phone_str) >= 7 and any(c.isdigit() for c in phone_str)

def validate_records(records):
    valid = []
    invalid = []

    for r in records:
        try:
            age = r.get("age")
            if (
                r.get("name")
                and is_valid_email(r.get("email"))
                and is_valid_phone(r.get("phone"))
                and age is not None
                and isinstance(age, (int, str))
                and 18 <= int(age) <= 100  # reasonable age range
            ):
                valid.append(r)
            else:
                invalid.append(r)
        except (ValueError, TypeError):
            invalid.append(r)

    print(f"Valid: {len(valid)}, Invalid: {len(invalid)}")
    return valid, invalid  # Return both valid and invalid