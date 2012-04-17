# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = %q{activerecord-jdbc-adapter}
  s.version = "1.2.1"

  s.required_rubygems_version = Gem::Requirement.new(">= 0") if s.respond_to? :required_rubygems_version=
  s.authors = [%q{Nick Sieger, Ola Bini and JRuby contributors}]
  s.date = %q{2011-11-23}
  s.description = %q{activerecord-jdbc-adapter is a database adapter for Rails\' ActiveRecord
component that can be used with JRuby[http://www.jruby.org/]. It allows use of
virtually any JDBC-compliant database with your JRuby on Rails application.}
  s.email = %q{nick@nicksieger.com, ola.bini@gmail.com}
  s.homepage = %q{https://github.com/jruby/activerecord-jdbc-adapter}
  s.rdoc_options = [%q{--main}, %q{README.txt}, %q{-SHN}, %q{-f}, %q{darkfish}]
  s.require_paths = [%q{lib}]
  s.rubyforge_project = %q{jruby-extras}
  s.rubygems_version = %q{1.8.9}
  s.summary = %q{JDBC adapter for ActiveRecord, for use within JRuby on Rails.}

  if s.respond_to? :specification_version then
    s.specification_version = 3

    if Gem::Version.new(Gem::VERSION) >= Gem::Version.new('1.2.0') then
    else
    end
  else
  end
end
