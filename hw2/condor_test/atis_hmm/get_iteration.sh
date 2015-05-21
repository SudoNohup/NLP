seq 0 9| xargs -I % bash -c "tail -9 exp%.err | head -1 | awk {'print $1'}"
