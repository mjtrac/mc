find . -name "*.java" -exec sh -c '
  for file do
    if ! grep -q "Licensed under the Apache License" "$file"; then
      tmp=$(mktemp)
      cat apache_license_header.txt "$file" > "$tmp" && mv "$tmp" "$file"
      echo "Header inserted in $file"
    else
      echo "Header already present in $file"
    fi
  done
' sh {} +
