seq 0 1 9 | xargs -I % bash -c "cat exp%.err | grep '=0.'"
