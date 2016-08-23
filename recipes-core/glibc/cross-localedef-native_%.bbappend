# This is a backport of http://git.yoctoproject.org/cgit.cgi/poky/commit/meta/recipes-core/glibc?id=c57ba52b70d803c737d694eb3046d8ede6703749 can be removed with newer poky versions
SRC_URI_append = " \
                  file://strcoll-Remove-incorrect-STRDIFF-based-optimization-.patch \
                 "
